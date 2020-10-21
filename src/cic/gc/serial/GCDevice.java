/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

import cic.gc.util.GCReadRequestCaller;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Data;
import lombok.ToString;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;

/**
 *
 * @author prera
 */
@Data
@ToString
public class GCDevice implements GCReadRequestCaller {
    // json properties
    private String deviceName;
    private int slaveAddress;
    private ObservableList<GCRegister> deviceFields;
    private ObservableList<GCFetcher> deviceFetchers;

    // other properties
    private GCBus bus = null;
    private ObservableMap<Integer, GCRegister> registers;
    
    @Override
    public void processReadResponse(ReadMultipleRegistersResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void init(GCBus gcBus) {
        bus = gcBus;
        // Initialize the registers map
        for(GCRegister gr:deviceFields){
            registers.put(gr.getRegisterNo(), gr);
        }
        // Initialize all fetchers
        Timer timer = new Timer();
        for(GCFetcher fetcher:deviceFetchers){
            fetcher.init(this, timer);
        }
    }
}
