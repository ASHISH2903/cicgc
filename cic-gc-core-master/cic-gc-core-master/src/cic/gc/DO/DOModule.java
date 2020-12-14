/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cic.gc.DO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DOModule {

    @JsonProperty
    private int address;

    @JsonProperty
    private int value;

    @JsonIgnore
    private int file;

    public DOModule() {
    }

    public DOModule(int address, int value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "address=" + address + ", value=" + value;
    }

    public boolean checkI2C() {

        int O_RDWR = 2;
        String fileName = "/dev/i2c-2";

        // Open the File.
        file = CLibrary.INSTANCE.open(fileName, O_RDWR);
        if (file < 0) {
            System.out.println("Failed to open I2C-2 file");
            return false;
        }
        int i2c_slaveId = 0x0703;
        System.setProperty("jna.nosys", "true");

        int ioctl = CLibrary.INSTANCE.ioctl(file, i2c_slaveId, address);

        if (ioctl < 0) {
            System.out.println("ioctl Called Failed");
            return false;
        }

        byte[] buf = {0, 0};

        int writeReturn = CLibrary.INSTANCE.write(file, buf, 2);

        if (writeReturn != 2) {
            System.out.println("Write Failed");
            return false;
        }
        return true;
    }

    public boolean setBit(int which, int doModuleNo) {
        System.out.println("Address : " + address);
        System.out.println("Selected DO Module : " + doModuleNo);
        System.out.println("Selected Bit no : " + which);
        value |= ((0x01) << which);

        byte buffer[] = {(byte) 0X12, (byte) value};
        int writeReturn = CLibrary.INSTANCE.write(file, buffer, 2);

        if (writeReturn != 2) {
            System.out.println("Inside Set->Failed to Turn ON LED");
            System.out.println("write Return = " + writeReturn);
            CLibrary.INSTANCE.close(file);
            return false;
        } else {
            return true;
        }
    }

    public boolean resetBit(int which, int doModuleNo) {
        System.out.println("Address : " + address);
        System.out.println("Selected DO Module : " + doModuleNo);
        System.out.println("Selected Bit no : " + which);
        value &= ~((0x01) << which);
        byte buffer[] = {(byte) 0X12, (byte) value};
        int writeReturn = CLibrary.INSTANCE.write(file, buffer, 2);

        if (writeReturn != 2) {
            System.out.println("Inside Reset->Failed to Turn Off LED");
            System.out.println("write Return = " + writeReturn);
            CLibrary.INSTANCE.close(file);
            return false;
        } else {
            return true;
        }
    }

    public void seeStatus(int i) {
        System.out.println("Module " + i + " value : " + String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0'));
    }
}
