/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.tcp;

import cic.gc.util.Repo;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
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

    String ipadd;
    String port;
    

    public void init(String ipPath) {
        try {
            System.out.println("Ip Path : "+ ipPath);
            FileReader reader = new FileReader(ipPath);
            Properties p = new Properties();
            p.load(reader);
            ipadd = p.getProperty("ipaddress").replace("\"","");
            port = p.getProperty("port");
            ModbusCoupler.getReference().setProcessImage(Repo.create().getSpi());
            ModbusCoupler.getReference().setMaster(false);
            ModbusCoupler.getReference().setUnitID(1);
            ModbusTCPListener listener = new ModbusTCPListener(3);
            listener.setPort(Integer.parseInt(port));
            listener.setAddress(InetAddress.getByName(ipadd));
            listener.start();
            System.out.println("Running Modbus TCP Server");
        } catch (UnknownHostException ex) {
            Logger.getLogger(MBTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MBTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("TCP IP IO EXCEPTION");
            Logger.getLogger(MBTcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
