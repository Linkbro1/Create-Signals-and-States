package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.Port;

public class SumModule extends Module {
    {
        this.itemName = "sum_module";
        this.width = 1;
        this.input = new Port[4];
        this.output = new Port[1];

        input[0] = new Port(1, 1);
        input[1] = new Port(1, 4);
        input[2] = new Port(1, 7);
        input[3] = new Port(1, 10);
        output[0] = new Port(1, 13);
    }

    @Override
    public void process() {
        int result = 0;
        for (int i = 0; i < 3; i++) {
            result += input[i].value;
        }

        output[0].value = Math.clamp(result, -256, 256);
        return;
    }
}
