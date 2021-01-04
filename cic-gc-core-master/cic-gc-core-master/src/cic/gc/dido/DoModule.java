/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.dido;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author prera
 */
public class DoModule {

    private int address;
    private int sequence;
    // initially, the value should all be set to zero
    private int currentValue = 0x0;
    // dirty should be set only when the output requires an update
    private boolean dirty = false;

    public DoModule(int address, int sequence) {
        this.address = address;
        this.sequence = sequence;
        // output all 8 bits as 0 to initialize to a known value

        // start a timer to check the dirty flag and flush the output
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
        // and since there is a change, set dirty flag to true

        dirty = true;
        // no further action - actual writing will be in timer        
    }

    synchronized boolean getBit(int bitIndex){
        // if the index is out of range, send false
        if(bitIndex < 0 || bitIndex > 7){
            return false;
        }
        // otherwise, check the value of currentValue and reply
        return ((currentValue & (0x1 << bitIndex)) != 0);
    }
    
    synchronized private void flushOutput() {
        // reset dirty flag first
        dirty = false;
        // flush the 8 bits currentValue to the corresponding address
    }
}
