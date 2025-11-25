package net.linkbro.createsignalsandstates.classes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public class Port {
    public int value;
    public Vec2 position;

    public Port(int x, int y) {
        this.position = new Vec2(x, y);
        this.value = 0;
    }

    public static CompoundTag serializeNBT(Port port) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("value", port.value);
        tag.putInt("posX", (int) port.position.x);
        tag.putInt("posY", (int) port.position.y);
        return tag;
    }

    public static Port deserializeNBT(CompoundTag compoundTag) {
        int portX = compoundTag.getInt("posX");
        int portY = compoundTag.getInt("posY");

        Port port = new Port(portX, portY);
        port.value = compoundTag.getInt("value");
        return port;
    }
}
