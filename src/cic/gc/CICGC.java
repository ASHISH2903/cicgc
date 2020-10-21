/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc;

import cic.gc.serial.GCBus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public static void main(String[] args) {
        Config.setup(args, false);
        try {
            String path = Config.getHomePath();
            path += File.separator;
            path += Config.argValues.get("busconfig");
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<GCBus> lstGcBus = new ArrayList<>();
            lstGcBus = mapper.readValue(new File(path), lstGcBus.getClass());
            for(int i = 0; i < lstGcBus.size(); i ++){
                System.out.println("..............................." + i + "...............");
                System.out.println(lstGcBus.get(i));
                lstGcBus.get(i).init();
            }
        } catch (IOException ex) {
            Logger.getLogger(CICGC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
