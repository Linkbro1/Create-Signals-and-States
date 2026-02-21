package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.linkbro.createsignalsandstates.util.SerializationUtils;
import net.linkbro.createsignalsandstates.util.linkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ComputerRackBlockEntity extends BlockEntity {
    public Module[] Modules = new Module[8];

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
        ListTag modulesListTag = new ListTag(8);
        SlotOnRack testSOR;
        for (int i = 0; i < Modules.length; i++) {
            testSOR = new SlotOnRack(this, i);
            if (linkUtils.isModuleOrigin(testSOR)) {
                CompoundTag moduleTag = SerializationUtils.serializeModule(Modules[i]);
                moduleTag.putInt("slot", i);
                modulesListTag.add(moduleTag);
            }
        }
        tag.put("modules", modulesListTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
        this.Modules = new Module[8];
        ListTag modulesListTag = tag.getList("modules", Tag.TAG_COMPOUND);
        for (int i = 0; i < modulesListTag.size(); i++) {
            CompoundTag moduleTag = modulesListTag.getCompound(i);
            int slot = moduleTag.getInt("slot");
            Module module = SerializationUtils.deserializeModule(moduleTag);
            if (module != null) {
                addModule(module, new SlotOnRack(this, slot));
            }
        }
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

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public ComputerRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(SNSBlockEntities.RACK_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void dropModules() {
        if (this.level.isClientSide || this.level == null) {
            return;
        }
        SlotOnRack removalSOR;
        SimpleContainer returnItems = new SimpleContainer(9);
        for (int i = 0; i < Modules.length; i++) {
            returnItems.setItem(i, ModuleFactory.ItemStackFromModule(this.Modules[i]));
            removalSOR = new SlotOnRack(this, i);
            removeModule(removalSOR);
        }
        Containers.dropContents(this.level, this.worldPosition, returnItems);
    }

    public boolean addModule(Module module, SlotOnRack slotOnRack) {
        if (slotOnRack == null || module == null || this.level == null) {
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
        targetSOR.rack.setChanged();
        level.sendBlockUpdated(targetSOR.rack.worldPosition, targetSOR.rack.getBlockState(),
                targetSOR.rack.getBlockState(), 3);
        SlotOnRack nextSOR;
        for (int i = 0; i < module.width; i++) {
            targetSOR.rack.Modules[targetSOR.slot] = module;
            nextSOR = linkUtils.getNextSlotOnRack(targetSOR);
            if (nextSOR.rack != targetSOR.rack) {
                nextSOR.rack.setChanged();
                level.sendBlockUpdated(nextSOR.rack.worldPosition, nextSOR.rack.getBlockState(),
                        nextSOR.rack.getBlockState(), 3);
            }
            targetSOR = nextSOR;
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
        targetSOR.rack.setChanged();
        level.sendBlockUpdated(targetSOR.rack.worldPosition, targetSOR.rack.getBlockState(),
                targetSOR.rack.getBlockState(), 3);
        SlotOnRack nextSOR = linkUtils.getNextSlotOnRack(targetSOR);
        if (nextSOR == null) {
            return true;
        }
        while (nextSOR.rack.Modules[nextSOR.slot] == moduleToRemove) {
            nextSOR.rack.Modules[nextSOR.slot] = null;
            if (nextSOR.rack != targetSOR.rack) {
                nextSOR.rack.setChanged();
                level.sendBlockUpdated(nextSOR.rack.worldPosition, nextSOR.rack.getBlockState(),
                        nextSOR.rack.getBlockState(), 3);
            }
            nextSOR = linkUtils.getNextSlotOnRack(nextSOR);
            if (nextSOR == null) {
                return true;
            }
        }

        return true;
    }

}
