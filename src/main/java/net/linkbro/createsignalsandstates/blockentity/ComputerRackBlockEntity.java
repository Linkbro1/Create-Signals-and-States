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

    public boolean addModule(Module module, SlotOnRack slotOnRack) {
        if (slotOnRack == null || module == null) {
            return false;
        }

        SlotOnRack targetSOR = slotOnRack;
        for (int i = 0; i < module.width; i++) {
            if (targetSOR == null || targetSOR.rack.Modules[targetSOR.slot] != null) {
                return false;
            }
            targetSOR = linkUtils.getNextSlotOnRack(targetSOR);
        }

        targetSOR = slotOnRack;
        for (int i = 0; i < module.width; i++) {
            targetSOR.rack.Modules[targetSOR.slot] = module;
            targetSOR = linkUtils.getNextSlotOnRack(targetSOR);
        }
        return true;
    }

    public boolean removeModule(SlotOnRack slotOnRack) {
        if (slotOnRack == null) {
            return false;
        }
        Module moduleToRemove = slotOnRack.rack.Modules[slotOnRack.slot];
        if (moduleToRemove == null) {
            return false;
        }

        SlotOnRack targetSOR = slotOnRack;
        while (!linkUtils.isModuleOrigin(targetSOR)) {
            targetSOR = linkUtils.getPreviousSlotOnRack(targetSOR);
        }

        targetSOR.rack.Modules[targetSOR.slot] = null;
        SlotOnRack nextSOR = linkUtils.getNextSlotOnRack(targetSOR);
        if (nextSOR == null) {
            return true;
        }
        while (nextSOR.rack.Modules[nextSOR.slot] == moduleToRemove) {
            nextSOR.rack.Modules[nextSOR.slot] = null;
            nextSOR = linkUtils.getNextSlotOnRack(nextSOR);
            if (nextSOR == null) {
                return true;
            }
        }

        return true;
    }

}
