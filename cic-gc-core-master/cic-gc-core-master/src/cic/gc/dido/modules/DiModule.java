package cic.gc.dido.modules;

import cic.gc.dido.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Timer;
import java.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DiModule extends Device {

    private int diAddress;
    private int diSequence;
    private int diSize;
    // initially, the value should all be set to zero
    @JsonIgnoreProperties
    private int currentValue = 0x0;
    // dirty should be set only when the output requires an update
    @JsonIgnoreProperties
    private boolean dirty = false;

    public DiModule(Integer address) throws Exception {
        super(address);
    }

    public DiModule(DiModule diModule) throws Exception {
        super(diModule.diAddress);
        this.diAddress = diModule.diAddress;
        this.diSequence = diModule.diSequence;
        this.diSize = diModule.diSize;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (dirty) {
                    flushOutput();
                }
            }
        }, 0, 200);
    }

    @Override
    public void initialize() {
        send(0x00, 0x00);
        send(0x12, 0x00);
    }

    public Integer getAll() {
        return receive(0x12);
    }

    synchronized private void flushOutput() {
        // reset dirty flag first
        dirty = false;
        // flush the 8 bits currentValue to the corresponding diAddress
    }
}
