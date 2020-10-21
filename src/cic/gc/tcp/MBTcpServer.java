/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.tcp;

import lombok.Data;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 *
 * @author prera
 */
@Data
public class MBTcpServer {
    SimpleProcessImage spi = null;
    public void init(){
        Register r = new SimpleRegister();
    }
}
