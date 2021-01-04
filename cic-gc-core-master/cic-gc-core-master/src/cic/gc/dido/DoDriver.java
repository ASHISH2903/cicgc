/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.dido;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author prera
 */
public class DoDriver {
    String i2cFile;
    HashMap<Integer, DoModule> mapDoModules = new HashMap<>();
    public DoDriver(String i2cFile) {
        // initialize i2c file
        this.i2cFile = i2cFile;
        // do all initialization. if required a file, declare as a private
        // property and open here
        
        // Read the JSON file here
        readJson();
    }

    private void readJson() {
        // as a new module is found, initialize it and add to the lstModules
        // the modules should be added in the mapDoModules, with its sequence
        // as the map key, and the actual object as value
        
        // pass the i2c file reference to the modules if required while initializing
    }
    
    public void operateOutput(int index, boolean set){
        // set argument should be true to set bit, false to reset
        // find which sequence to operate
        int moduleIndex = index / 8;
        int bitIndex = index % 8;
        // get the sequences module from map
        DoModule module = mapDoModules.get(moduleIndex);
        // if not a valid module, return without doing anything
        if(module == null){
            return;
        }
        // otherwise, operate the value
        if(set){
            module.setBit(bitIndex);
        } else {
            module.resetBit(bitIndex);
        }
    }
    
    
}
