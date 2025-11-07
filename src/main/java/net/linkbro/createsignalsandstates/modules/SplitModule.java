package net.linkbro.createsignalsandstates.modules;

import net.linkbro.createsignalsandstates.abstractclasses.Module;
import net.linkbro.createsignalsandstates.abstractclasses.Port;

public class SplitModule extends Module{{
        this.itemName = "split_module";
        this.width = 1;
        this.input = new Port[1];
        this.output = new Port[4];

        this.input[0] = new Port(1,1);
        this.output[0] = new Port(1,4);
        this.output[1] = new Port(1,7);
        this.output[2] = new Port(1,10);
        this.output[3] = new Port(1,13);
    }
        public SplitModule() {}

        @Override
        public void process() {
            int result = Math.clamp(this.input[0].value,-256,256);

            for (int i = 0; i < 3; i++) {
                output[i].value = result;
            }

            return;
        }
}
