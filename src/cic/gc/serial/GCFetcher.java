/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

import cic.gc.util.GCReadRequest;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author prera
 */
@Data
@ToString
public class GCFetcher {
    // json properties
    private int referenceNo;
    private int totalCount;
    private int fetchPeriod;
    
    // other properties
    private GCDevice device;

    void init(GCDevice gd, Timer timer) {
        device = gd;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GCReadRequest rr = new GCReadRequest();
                rr.setCaller(device);
                rr.setCount(totalCount);
                rr.setReference(referenceNo);
                rr.setSlaveId(device.getSlaveAddress());
                device.getBus().addReadRequest(rr);
            }
        }, 0, fetchPeriod);
    }
}
