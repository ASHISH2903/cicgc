/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.tcp;

import cic.gc.util.Repo;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 *
 * @author prera
 */
@Data
public class MBTcpServer {
    
    public void init(){
        try {
            ModbusCoupler.getReference().setProcessImage(Repo.create().getSpi());
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(1);
            ModbusTCPListener listener = new ModbusTCPListener(3);
            listener.setPort(Modbus.DEFAULT_PORT);
            listener.setAddress(InetAddress.getByName("192.168.1.80"));
            listener.start();
            System.out.println("Running Modbus TCP Server");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MBTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
