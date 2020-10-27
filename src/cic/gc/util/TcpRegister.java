/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.util;

import cic.gc.serial.GCBus;
import cic.gc.serial.GCDevice;
import cic.gc.serial.GCRegister;
import net.wimpi.modbus.procimg.Register;

/**
 *
 * @author prera
 */
public class TcpRegister implements Register {

    protected byte[] register = new byte[2];

    public TcpRegister(GCRegister serialRegister) {
        this.serialRegister = serialRegister;
    }
    
    private GCRegister serialRegister;

    public GCRegister getSerialRegister() {
        return serialRegister;
    }

    public void setSerialRegister(GCRegister serialRegister) {
        this.serialRegister = serialRegister;
    }
    
    @Override
    public void setValue(int v) {
        setValue((short)v);
        
    }

    @Override
    public void setValue(short s) {
        register[0] = (byte)((s >> 8) & 0xff);
        register[1] = (byte)(s & 0xff);
        System.out.println("Setting TCP Register value to " + s);
        // when setting value, also write to the serial device if it is writable
        if(serialRegister.isTcpWritable()){
            System.out.println("Register is writable");
            GCWriteRequest gwr = new GCWriteRequest();
            gwr.setData(new Register[]{this});
            gwr.setReference(serialRegister.getRegisterNo());
            gwr.setSlaveId(serialRegister.getDevice().getSlaveAddress());
            serialRegister.getDevice().getBus().addWriteRequest(gwr);
        }
    }

    @Override
    public void setValue(byte[] bytes) {
        if(bytes.length < 2){
            throw new IllegalArgumentException();
        } else {
            short s = (short)(((bytes[0] << 8) & 0xff00) | (bytes[1] & 0xff));
            setValue(s);
        }
    }

    @Override
    public int getValue() {
        return ((register[0] & 0xff) << 8) | (register[1] & 0xff);
    }

    @Override
    public int toUnsignedShort() {
        return ((register[0] & 0xff) << 8) | (register[1] & 0xff);
    }

    @Override
    public short toShort() {
        return (short)toUnsignedShort();
    }

    @Override
    public byte[] toBytes() {
        return register;
    }

    // this is called when reading serial register
    // all other set values may be called by modbus client
    public void setValue(int value, boolean auto) {
        if(!auto){
            register[0] = (byte)((value >> 8) & 0xff);
            register[1] = (byte)(value & 0xff);
        }
    }
    
}
