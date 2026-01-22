package net.linkbro.createsignalsandstates.classes;

import net.minecraft.world.phys.Vec2;

public class Port {
    public int value;
    public Vec2 position;

    public Port(int x, int y) {
        this.position = new Vec2(x, y);
        this.value = 0;
    }

}
