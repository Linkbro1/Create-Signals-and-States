package net.linkbro.createsignalsandstates.classes;

import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;

public class SlotOnRack {
    public ComputerRackBlockEntity rack;
    public int slot;

    public SlotOnRack(ComputerRackBlockEntity rack, int slot) {
        this.rack = rack;
        this.slot = slot;
    }

}
