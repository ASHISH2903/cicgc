package cic.gc.DO.device;

public class MCP230XX extends Device {

    public MCP230XX(Integer doNumber) throws Exception {
        super(doNumber);
    }

    @Override
    public void initialize() {
        //System.out.println("Init called");
        send(0x00, 0x00);
        send(0x12, 0x00);
    }

    public Integer getAll() {
        return receive(0x12);
    }

    // This is not correct.
    public Integer getPin(Integer pin) {
        return getAll() << pin;
    }

    public void setPin(Integer pin) {
        // Read the current GPIO state.
        int data = getAll();
        data = data | (0x01 << pin);
        send(0x12, data);
    }

    public void resetPin(Integer pin) {
        // Read the current GPIO state.
        int data = getAll();
        data = data & ~(0x01 << pin);
        send(0x12, data);
    }
}
