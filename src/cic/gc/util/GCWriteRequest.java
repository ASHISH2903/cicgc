/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.util;

import lombok.Data;
import net.wimpi.modbus.procimg.Register;

/**
 *
 * @author prera
 */
@Data
public class GCWriteRequest {
    private int reference;
    private Register[] data;
    private int slaveId;
}
