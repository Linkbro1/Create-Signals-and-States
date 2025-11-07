package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.abstractclasses.Module;
import net.linkbro.createsignalsandstates.abstractclasses.Port;

public class AndModule extends Module{{
        this.itemName = "and_module";
        this.width = 1;
        this.input = new Port[2];
        this.output = new Port[1];

        this.input[0] = new Port(1,1);
        this.input[1] = new Port(1,7);
        this.output[0] = new Port(1,13);
    }
        public AndModule() {}

        @Override
        public void process() {
            return;
        }
}
