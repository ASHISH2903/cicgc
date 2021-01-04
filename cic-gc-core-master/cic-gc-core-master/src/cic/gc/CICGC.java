/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc;

import cic.gc.util.DoModule;
import cic.gc.serial.GCBus;
import cic.gc.util.Repo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
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

            DoModule myModule[];
            myModule = mapper.readValue(new File(path), DoModule[].class);
            for (int i = 0; i < myModule.length; i++) {
               // System.out.println("..............................." + i + "...............");
               // System.out.println("Data : " + myModule[i].toString());
                DoModule doModule = myModule[i];
                doModule.I2Cinit();
            }

        } catch (IOException ex) {
            Logger.getLogger(CICGC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CICGC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/*which = 15;
            rem = which % 8;
            quo = which / 8;
            switch (quo) {
                case 0:
                    myModule[quo].i2cdata(myModule[quo].getAddress(), quo, rem);
                    break;
                case 1:
                    myModule[quo].i2cdata(myModule[quo].getAddress(), quo, rem);
                    break;
                case 2:
                    myModule[quo].i2cdata(myModule[quo].getAddress(), quo, rem);
                    break;
                case 3:
                    myModule[quo].i2cdata(myModule[quo].getAddress(), quo, which);
                    break;
            }*/
