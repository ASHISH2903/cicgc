package cic.gc.util;

import cic.gc.DO.device.MCP230XX;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoModule {

    @JsonProperty
    private int doSequence;

    @JsonProperty
    private int doAddress;

    @JsonProperty
    private int doSize;

    MCP230XX gpio;

    @JsonIgnore
    private ArrayBlockingQueue<GCReadRequest> setPinQueue = new ArrayBlockingQueue<>(100);
    @JsonIgnore
    private ArrayBlockingQueue<GCWriteRequest> resetPinQueue = new ArrayBlockingQueue<>(50);
    @JsonIgnore
    private final int setPinProcessTime = 5;
    @JsonIgnore
    private final int resetPinProcessTime = 10;

    @JsonIgnore
    private boolean lock = true;

    doModuleDummyData DoModuleDummyData;

    public DoModule() {
    }

    public DoModule(int doSequence, int doAddress, int doSize) {
        this.doSequence = doSequence;
        this.doAddress = doAddress;
        this.doSize = doSize;
    }

    public int getDoSequence() {
        return doSequence;
    }

    public void setDoSequence(int doSequence) {
        this.doSequence = doSequence;
    }

    public int getDoAddress() {
        return doAddress;
    }

    public void setDoAddress(int doAddress) {
        this.doAddress = doAddress;
    }

    public int getDoSize() {
        return doSize;
    }

    public void setDoSize(int doSize) {
        this.doSize = doSize;
    }

    public void I2Cinit() throws Exception {
        gpio = new MCP230XX(doAddress);
        gpio.initialize();
        System.out.println("doAddress: " + doAddress + " doSequence: " + doSequence + " doSize: " + doSize);

        Timer timer = new Timer();

        // read queue processing
        timer.schedule(new DoModule.setPinTask(), 0, this.setPinProcessTime * 1000);
        // write queue processing
        timer.schedule(new DoModule.resetPinTask(), 0, this.resetPinProcessTime * 1000);
    }

    private class setPinTask extends TimerTask {

        @Override
        public void run() {

            // wait for the lock
            while (!lock) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DoModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lock = false;
            //System.out.println("Set pin timer called");

            try {
                System.out.println(DoModuleDummyData.getQuo());
            } catch (Exception ex) {
                System.out.println("Data not found");
            }
            if (DoModuleDummyData.getQuo() == getDoSequence()) {
                System.out.println("DO Address : " + getDoAddress());
                gpio.setPin(DoModuleDummyData.getRem());
            }
            lock = true;

        }

    }

    private class resetPinTask extends TimerTask {

        @Override
        public void run() {

            // wait for the lock
            while (!lock) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DoModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lock = false;
            //System.out.println("ReSet pin timer called");

            if (DoModuleDummyData.getQuo() == getDoSequence()) {
                System.out.println("DO Address : " + getDoAddress());
                gpio.resetPin(DoModuleDummyData.getRem());
            }
            lock = true;

        }

    }
}
