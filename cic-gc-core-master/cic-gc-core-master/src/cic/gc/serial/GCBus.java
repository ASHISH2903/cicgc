/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

import cic.gc.util.GCReadRequest;
import cic.gc.util.GCWriteRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

/**
 *
 * @author prera
 */
@Data
@AllArgsConstructor

public class GCBus {

    // from the json file
    private String portName;
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private String parity;
    private String busName;
    private int readQueueProcessTime;
    private int writeQueueProcessTime;
    private List<GCDevice> devices;

    // other properties
    @JsonIgnore
    private SerialConnection con = null;
    @JsonIgnore
    private boolean lock = true;
    @JsonIgnore
    private ArrayBlockingQueue<GCReadRequest> readQueue = new ArrayBlockingQueue<>(100);
    @JsonIgnore
    private ArrayBlockingQueue<GCWriteRequest> writeQueue = new ArrayBlockingQueue<>(50);

    /**
     * Make a connection to the serial port
     */
    public boolean connect() {
        try {
            SerialParameters param = new SerialParameters();
            param.setPortName(portName);
            param.setBaudRate(baudRate);
            param.setDatabits(dataBits);
            param.setStopbits(stopBits);
            param.setParity("None");
            param.setEncoding(Modbus.SERIAL_ENCODING_RTU);
            param.setEcho(false);
            con = new SerialConnection(param);
            con.open();

            System.out.println("Port opened for modbus");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void fetchRegisters() {
        try {
            ReadMultipleRegistersRequest rmrr = new ReadMultipleRegistersRequest(4096, 2);
            rmrr.setUnitID(4);
            ModbusSerialTransaction tran = new ModbusSerialTransaction(rmrr);
            tran.setSerialConnection(con);

            tran.execute();
            ReadMultipleRegistersResponse rms = (ReadMultipleRegistersResponse) tran.getResponse();
            for (int i = 0; i < rms.getWordCount(); i++) {
                System.out.println("Value at " + i + " is " + rms.getRegisterValue(i));
            }

        } catch (ModbusSlaveException ex) {
            Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ModbusException ex) {
            Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init() {
        System.out.println("Initilizing bus...");
        // make a connection
        connect();
        System.out.println("Bus connected");
        for (int i = 0; i < this.devices.size(); i++) {
            devices.get(i).init(this);
        }
        Timer timer = new Timer();
        // read queue processing
        timer.schedule(new ReadQueueTask(), 0, this.readQueueProcessTime * 1000);
        // write queue processing
        timer.schedule(new WriteQueueTask(), 0, this.writeQueueProcessTime * 1000);
    }

    public void addReadRequest(GCReadRequest rr) {
        while (true) {
            if (readQueue.add(rr)) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addWriteRequest(GCWriteRequest wr) {
        while (true) {
            if (writeQueue.add(wr)) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class ReadQueueTask extends TimerTask {

        GCReadRequest rr;

        @Override
        public void run() {

            if (con == null) {
                return;
            }
            // read queue is processed one item at a time
            while (!readQueue.isEmpty()) {
                try {
                    // wait for the lock
                    while (!lock) {
                        Thread.sleep(10);
                    }
                    lock = false;
                    rr = readQueue.take();
                    ReadMultipleRegistersRequest rmrr = new ReadMultipleRegistersRequest(rr.getReference(), rr.getCount());
                    rmrr.setHeadless();
                    rmrr.setUnitID(rr.getSlaveId());
                    ModbusSerialTransaction tran = new ModbusSerialTransaction();
                    tran.setRequest(rmrr);
                    tran.setSerialConnection(con);
                    tran.execute();
                    ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) tran.getResponse();
                    rr.getCaller().processReadResponse(res, rr.getReference(), rr.getCount());
                    lock = true;
                } catch (InterruptedException | ModbusException ex) {
                    System.out.println("Again Request for Reading");
                    addReadRequest(rr);
                    Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private class WriteQueueTask extends TimerTask {

        @Override
        public void run() {

            if (con == null) {
                return;
            }
            // wait for the lock
            while (!lock) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lock = false;
            // write queue is processed all at once
            while (!writeQueue.isEmpty()) {
                try {
                    GCWriteRequest wr = writeQueue.take();
                    WriteMultipleRegistersRequest wmrr = new WriteMultipleRegistersRequest();
                    System.out.println("Writing to " + wr.getReference());
                    System.out.println("Data is " + wr.getData()[0].getValue());
                    System.out.println("Slave " + wr.getSlaveId());
                    wmrr.setReference(wr.getReference());
                    wmrr.setRegisters(wr.getData());
                    wmrr.setHeadless();
                    wmrr.setUnitID(wr.getSlaveId());
                    ModbusSerialTransaction tran = new ModbusSerialTransaction();
                    tran.setRequest(wmrr);
                    tran.setSerialConnection(con);
                    tran.execute();
                    WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) tran.getResponse();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ModbusSlaveException ex) {
                    Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ModbusException ex) {
                    Logger.getLogger(GCBus.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lock = true;
        }

    }

    public GCBus() {
    }

}
