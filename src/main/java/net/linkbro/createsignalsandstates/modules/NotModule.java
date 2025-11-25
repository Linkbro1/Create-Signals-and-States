package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.Port;

public class NotModule extends Module {
    {
        this.itemName = "not_module";
        this.width = 1;
        this.input = new Port[1];
        this.output = new Port[1];

        this.input[0] = new Port(1, 1);
        this.output[0] = new Port(1, 12);
    }

    public NotModule() {
    }

    @Override
    public void process() {
        return;
    }
}
