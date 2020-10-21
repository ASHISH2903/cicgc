/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.serial;

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
    private int value;
}
