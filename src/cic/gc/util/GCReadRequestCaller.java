/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.util;

import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;

/**
 *
 * @author prera
 */
public interface GCReadRequestCaller {
    void processReadResponse(ReadMultipleRegistersResponse response);
}
