/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

import cic.gc.util.Repo;
import cic.gc.util.TcpRegister;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author prera
 */
@Data
@ToString
public class GCRegister {

    // json properties
    private int registerNo;
    private String registerName;
    private int tcpRegisterNo = -1;
    private boolean tcpWritable = false;

    // other properties
    @JsonIgnore
    private int registerValue;
    @JsonIgnore
    private GCDevice device;
    @JsonIgnore
    TcpRegister tcpRegister = null;

    void setDevice(GCDevice device) {
        this.device = device;
    }

    void init() {
        // create a tcp register and add that to tcp repository
        if (tcpRegisterNo != -1) {
            tcpRegister = new TcpRegister(this);
            Repo.create().getSpi().setRegister(tcpRegisterNo, tcpRegister);
        }
    }

    public void setRegisterValue(int value) {
        if (tcpRegister != null) {
            
            tcpRegister.setValue(value, false);
        }
        
        this.registerValue = value;
    }
}
