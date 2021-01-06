/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.dido.drivers;

import cic.gc.dido.modules.DiModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import suyojancommon.Config;

/**
 *
 * @author ASHISH
 */
public class DiDriver {

    HashMap<Integer, DiModule> mapDiModules = new HashMap<>();

    public DiDriver() throws IOException, Exception {
        readJson();
    }
    
    private void readJson() throws IOException, Exception {
        // as a new module is found, initialize it and add to the lstModules
        // the modules should be added in the mapDoModules, with its sequence
        // as the map key, and the actual object as value

        // pass the i2c file reference to the modules if required while initializing
        String path = Config.getHomePath();
        path += File.separator;
        path += Config.argValues.get("diconfig");
        System.out.println("Path "+path);
        ObjectMapper mapper = new ObjectMapper();

        DiModule diModules[];
        diModules = mapper.readValue(new File(path), DiModule[].class);

        for (int i = 0; i < diModules.length; i++) {
            DiModule diModule = new DiModule(diModules[i]);
            diModule.initialize();
            System.out.println("Value : "+diModule.getAll());
        }
    }
    /*@JsonProperty
    private int diAddress;

    @JsonProperty
    private int diSize;

    @JsonIgnore
    private int file;

    public DiDriver() {
    }

    public DiDriver(int diAddress, int diSize) {
        this.diAddress = diAddress;
        this.diSize = diSize;
    }

    public int getAddress() {
        return diAddress;
    }

    public void setAddress(int diAddress) {
        this.diAddress = diAddress;
    }

    public int getValue() {
        return diSize;
    }

    public void setValue(int diSize) {
        this.diSize = diSize;
    }

    @Override
    public String toString() {
        return "DoModule{" + "diAddress=" + diAddress + ", diSize=" + diSize;
    }

    public void i2cdata(int diAddress) throws Exception {
        System.out.println("Address : " + diAddress);
        DiModule gpio = new DiModule(diAddress);
        gpio.initialize();
        //System.out.println("Value :" + gpio.getAll());
    }*/
}
