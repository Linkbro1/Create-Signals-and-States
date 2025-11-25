package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.Port;

public class LinkModule extends Module {
    {
        this.itemName = "link_module";
        this.width = 2;
        this.input = new Port[1];
        this.output = new Port[1];

        this.input[0] = new Port(1, 12);
        this.output[0] = new Port(5, 12);
    }

    public LinkModule() {
    }

    @Override
    public void process() {
        return;
    }
}
