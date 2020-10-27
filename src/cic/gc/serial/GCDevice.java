/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

import cic.gc.util.GCReadRequestCaller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
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
    private List<GCRegister> deviceFields;
    private List<GCFetcher> deviceFetchers;

    // other properties
    @JsonIgnore
    private GCBus bus = null;
    @JsonIgnore
    private Map<Integer, GCRegister> registers = new HashMap<>();
    
    @Override
    public void processReadResponse(ReadMultipleRegistersResponse response, int ref, int count) {
        for(int i = 0; i < count; i ++){
            GCRegister gr = registers.get(ref + i);
            gr.setRegisterValue(response.getRegisterValue(i));
            //registers.put(ref + i, gr);
        }
    }

    void init(GCBus gcBus) {
        System.out.println("*** Initializing Device...");
        bus = gcBus;
        // Initialize the registers map
        for(GCRegister gr:deviceFields){
            gr.setDevice(this);
            gr.init();
            registers.put(gr.getRegisterNo(), gr);
        }
        // Initialize all fetchers
        Timer timer = new Timer();
        for(GCFetcher fetcher:deviceFetchers){
            fetcher.init(this, timer);
        }
    }
}
