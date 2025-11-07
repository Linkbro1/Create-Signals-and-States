package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.abstractclasses.Module;
import net.linkbro.createsignalsandstates.abstractclasses.Port;

public class BiasModule extends Module{{
        this.itemName = "bias_module";
        this.width = 2;
        this.input = new Port[1];
        this.output = new Port[1];

        this.input[0] = new Port(1,12);
        this.output[0] = new Port(5,12);
    }
        public BiasModule() {}

        @Override
        public void process() {
            return;
        }
}
