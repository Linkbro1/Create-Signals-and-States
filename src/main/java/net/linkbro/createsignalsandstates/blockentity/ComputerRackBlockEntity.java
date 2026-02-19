package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.linkbro.createsignalsandstates.util.linkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ComputerRackBlockEntity extends BlockEntity {
    public Module[] Modules = new Module[8];

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, Provider registries) {
        loadAdditional(tag, registries);
    }

    public ComputerRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(SNSBlockEntities.RACK_BLOCK_ENTITY.get(), pos, blockState);
    }

    public boolean addModule(Module module, SlotOnRack slotOnRack) { // REGRESSION: THIS IS NO LONGER ABLE TO HANDLE
                                                                     // MODULES MORE THAN 1 WIDTH
        if (slotOnRack == null || module == null) {
            return false;
        }
        // boolean isRoom = true;

        slotOnRack.rack.Modules[slotOnRack.slot] = module;
        return true;
    }

    public boolean removeModule(SlotOnRack slotOnRack) { // REGRESSION: THIS IS NO LONGER ABLE TO HANDLE MODULES MORE
                                                         // THAN 1 WIDTH
        return removeModule(slotOnRack, 0);
    }

    private boolean removeModule(SlotOnRack slotOnRack, int generation) {
        if (slotOnRack == null) {
            return false;
        }

        if (linkUtils.isModuleOrigin(slotOnRack)) {
            slotOnRack.rack.Modules[slotOnRack.slot] = null;
        } else {
            // call this on the origin
        }

        return true;
    }

}
