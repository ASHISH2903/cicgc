/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.dido.drivers;

import cic.gc.dido.modules.DiModule;
import cic.gc.dido.modules.DoModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import suyojancommon.Config;

/**
 *
 * @author prera
 */
public class DoDriver {

    HashMap<Integer, DoModule> mapDoModules = new HashMap<>();

    public DoDriver() throws IOException, Exception {
        readJson();
    }

    private void readJson() throws IOException, Exception {
        // as a new module is found, initialize it and add to the lstModules
        // the modules should be added in the mapDoModules, with its sequence
        // as the map key, and the actual object as value

        // pass the i2c file reference to the modules if required while initializing
        String path = Config.getHomePath();
        path += File.separator;
        path += Config.argValues.get("doconfig");
        ObjectMapper mapper = new ObjectMapper();

        DoModule doModules[];
        doModules = mapper.readValue(new File(path), DoModule[].class);

        for (int i = 0; i < doModules.length; i++) {
            DoModule doModule = new DoModule(doModules[i]);
            System.out.println(doModule);
            mapDoModules.put(doModule.getDoSequence(), doModule);
            doModule.initialize();
            //System.out.println("Value : "+DoModule.getAll());
        }
        operateOutput(7, true);
    }

    public void operateOutput(int index, boolean set) {
        // set argument should be true to set bit, false to reset
        // find which sequence to operate
        int moduleIndex = index / 8;
        int bitIndex = index % 8;
        // get the sequences module from map
        DoModule module = mapDoModules.get(moduleIndex);
        System.out.println("Module : " + module);
        // if not a valid module, return without doing anything
        if (module == null) {
            return;
        }
        // otherwise, operate the value
        if (set) {
            module.setBit(bitIndex);
//            System.out.println(bitIndex + " bit is set");
        } else {
            module.resetBit(bitIndex);
        }
    }

}
