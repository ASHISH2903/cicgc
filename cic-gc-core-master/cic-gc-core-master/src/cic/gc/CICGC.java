/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc;

import cic.gc.DO.DOModule;
import cic.gc.serial.GCBus;
import cic.gc.util.Repo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import suyojancommon.Config;

/**
 *
 * @author prera
 */
public class CICGC {

    /**
     * @param args the command line arguments
     */
    static int which, rem, quo, choice;

    public static void main(String[] args) {
        Config.setup(args, false);
        try {
            String path = Config.getHomePath();
            path += File.separator;
            path += Config.argValues.get("busconfig");
            //String ipPath = path+= Config.argValues.get("ipconfig");
            ObjectMapper mapper = new ObjectMapper();
            GCBus buses[];

            buses = mapper.readValue(new File(path), GCBus[].class);
            for (int i = 0; i < buses.length; i++) {
                System.out.println("..............................." + i + "...............");
                System.out.println(buses[i]);
                GCBus bus = buses[i];
                //bus.init();
            }
            System.out.println("Will start TCP server");
            path = Config.getHomePath();
            path += File.separator;
            path += Config.argValues.get("ipconfig");
            // run the tcp server only after the tcp registers are innitialized
            //Repo.create().getTcpServer(path);

            path = Config.getHomePath();
            path += File.separator;
            path += Config.argValues.get("i2cconfig");

            
            // For DO Module
            DOModule doModuleTest[];
            which = 7;
            rem = which % 8;
            quo = which / 8;
            doModuleTest = mapper.readValue(new File(path), DOModule[].class);
            for (int i = 0; i < doModuleTest.length; i++) {
                System.out.println(doModuleTest[i].toString());
                if (doModuleTest[i].checkI2C()) {
                    switch (quo) {
                        case 0:
                            doModuleTest[quo].setBit(rem, quo);
                            break;
                        case 1:
                            doModuleTest[quo].setBit(rem, quo);
                            break;
                        case 2:
                            doModuleTest[quo].resetBit(rem, quo);
                            break;
                        case 3:
                            doModuleTest[quo].resetBit(rem, quo);
                            break;
                        default:
                            System.out.println("Value can not be greater than 31..");
                            break;
                    }
                }
            }
            
            for(int i=0;i<doModuleTest.length;i++){
                doModuleTest[i].seeStatus(i);
            }

        } catch (IOException ex) {
            Logger.getLogger(CICGC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
