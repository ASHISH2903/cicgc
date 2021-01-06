/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.dido.modules;

import cic.gc.dido.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Data;

/**
 *
 * @author prera
 */
@Data
public class DoModule extends Device {

    private int doAddress;
    private int doSequence;
    private int doSize;
    // initially, the value should all be set to zero
    @JsonIgnoreProperties
    private int currentValue = 0x0;
    // dirty should be set only when the output requires an update
    @JsonIgnoreProperties
    private boolean dirty = false;

    private int file;

    public DoModule() {
    }

    public DoModule(DoModule doModule) throws Exception {
        super(doModule.doAddress);
        this.doAddress = doModule.doAddress;
        this.doSequence = doModule.doSequence;
        this.doSize = doModule.doSize;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (dirty) {
                    flushOutput();
                }
            }
        }, 0, 200);
    }

    @Override
    public void initialize() {
        send(0x00, 0x00);
        send(0x12, 0x00);
    }

    public Integer getAll() {
        return receive(0x12);
    }

    // the method is synchronized to other reads and writes
    synchronized public void setBit(int bitIndex) {
        // if the bit is not in valid range, reject and return
        if (bitIndex < 0 || bitIndex > 7) {
            return;
        }

        // check if the bit already is set
        if ((currentValue & (0x1 << bitIndex)) != 0) {
            return;
        }

        // otherwise setting is required
        // modify the currentValue to set the indexed bit
        currentValue |= (0x1 << bitIndex);
        send(0x12, currentValue);
        // and since there is a change, set dirty flag to true

        dirty = true;
        // no further action - actual writing will be in timer
    }

    synchronized public void resetBit(int bitIndex) {
        // if the bit is not in valid range, reject and return
        if (bitIndex < 0 || bitIndex > 7) {
            return;
        }
        // check if the bit already is reset
        if ((currentValue | ~(0x1 << bitIndex)) == 0) {
            return;
        }

        // otherwise resetting is required
        // modify the currentValue to set the indexed bit
        currentValue &= ~(0x1 << bitIndex);
        send(0x12, currentValue);
        // and since there is a change, set dirty flag to true

        dirty = true;
        // no further action - actual writing will be in timer        
    }

    synchronized private void flushOutput() {
        // reset dirty flag first
        dirty = false;
        // flush the 8 bits currentValue to the corresponding doAddress
    }
}

//synchronized boolean getBit(int bitIndex) {
//        // if the index is out of range, send false
//        if (bitIndex < 0 || bitIndex > 7) {
//            return false;
//        }
//        return ((currentValue & (0x1 << bitIndex)) != 0);
//    }
/*public void setPin(Integer pin, Boolean high) {
        // Read the current GPIO state.
        int data = getAll();
        if (high) {
            data = data | (0x01 << pin);
        } else {
            data = data & ~(0x01 << pin);
        }
        send(0x12, data);
}*/
