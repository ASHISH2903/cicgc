/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.util;

import lombok.Data;

/**
 *
 * @author prera
 */
@Data
public class GCReadRequest {
    private int reference;
    private int count;
    private GCReadRequestCaller caller;
    private int slaveId;
}
