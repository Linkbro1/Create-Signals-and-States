package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.block.ComputerRack;
import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.linkbro.createsignalsandstates.util.BlockInteractionUtils;
import net.linkbro.createsignalsandstates.util.SNSTags;
import net.linkbro.createsignalsandstates.util.SerializationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;

public class ComputerRackBlockEntity extends BlockEntity {
    public Module[] frontModules = new Module[4];
    public Module[] backModules = new Module[4];

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) { // TODO: fix this, something here is fucked,
                                                                          // it doesn't let the world save right
        super.saveAdditional(tag, registries);
        ListTag frontModuleList = new ListTag();
        ListTag backModuleList = new ListTag();
        CompoundTag emptyTag = new CompoundTag();

        for (int i = 0; i < frontModules.length; i++) {
            Module module = frontModules[i];
            SlotOnRack prevSOR = getPrevSlot(this, i, true);
            Module prevModule = prevSOR.slot != -1 ? prevSOR.rack.frontModules[prevSOR.slot] : null;

            if (module == null || module == prevModule) {
                frontModuleList.add(emptyTag);
            } else {
                frontModuleList.add(SerializationUtils.serializeModule(module));
            }
        }
        tag.put("frontModuleList", frontModuleList);

        for (int i = 0; i < backModules.length; i++) {
            Module module = backModules[i];
            SlotOnRack prevSOR = getPrevSlot(this, i, false);
            prevSOR.slot = prevSOR.slot > 4 ? prevSOR.slot - 4 : prevSOR.slot; // kinda hacky
            Module prevModule = prevSOR.slot != -1 ? prevSOR.rack.backModules[prevSOR.slot] : null;

            if (module == null || module == prevModule) {
                backModuleList.add(emptyTag);
            } else {
                backModuleList.add(SerializationUtils.serializeModule(module));
            }
        }
        tag.put("backModuleList", backModuleList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);

        ListTag frontModuleList = tag.getList("frontModuleList", Tag.TAG_COMPOUND);
        for (int i = 0; i < frontModuleList.size(); i++) {
            Module module = SerializationUtils.deserializeModule(frontModuleList.getCompound(i));
            if (module != null) {
                addModule(module, this, i);
            }
            // this.frontModules[i] = Module.deserializeNBT(moduleTag);
        }

        ListTag backModuleList = tag.getList("backModuleList", Tag.TAG_COMPOUND);
        for (int i = 0; i < backModuleList.size(); i++) {
            Module module = SerializationUtils.deserializeModule(backModuleList.getCompound(i));
            if (module != null) {
                addModule(module, this, i + 4);
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

    public ComputerRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(SNSBlockEntities.RACK_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void handleRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        int slot = getSlot(this, state, hitResult).slot;
        if (slot == -1)
            return; // didn't hit either the back or the front.

        if (!stack.isEmpty() && stack.is(SNSTags.Items.MODULES)) {
            // frontModules[slot] = ModuleFactory.moduleFromItemstack(stack);
            Module module = ModuleFactory.moduleFromItemstack(stack);
            if (addModule(module, this, slot) && !player.isCreative()) {
                stack.shrink(1);
            }
        }
        // player.displayClientMessage(Component.literal("slot hit: " + slot),true);
    }

    public void handleShiftRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        int slot = getSlot(this, state, hitResult).slot;
        if (slot == -1)
            return;
        ItemStack returnItem = ItemStack.EMPTY;

        if (stack.isEmpty()) {
            if (slot <= 3) {
                returnItem = ModuleFactory.ItemStackFromModule(frontModules[slot]);
                // frontModules[slot] = null;
                if (removeModule(frontModules[slot], this, slot, 0) && !player.isCreative()) {
                    player.setItemInHand(hand, returnItem);
                }
            } else {
                returnItem = ModuleFactory.ItemStackFromModule(backModules[slot - 4]);
                // backModules[slot-4] = null;
                if (removeModule(backModules[slot - 4], this, slot, 0) && !player.isCreative()) {
                    player.setItemInHand(hand, returnItem);
                }
            }
        }
    }

    public boolean addModule(Module module, ComputerRackBlockEntity rack, int slot) { // TODO: work this bs out
        boolean isRoom = true;
        boolean front = (slot <= 3 && slot >= 0);
        ComputerRackBlockEntity mainRack = rack;
        int mainSlot = slot;
        SlotOnRack nextSOR = getNextSlot(this, mainSlot, front);
        if (nextSOR.slot == -1 && module.width > 1) {
            return false;
        }

        if (front) {
            for (int i = 0; i < module.width - 1; i++) {
                if (nextSOR.rack.frontModules[nextSOR.slot] != null) {
                    isRoom = false;
                }
                nextSOR = getNextSlot(nextSOR.rack, nextSOR.slot, front);
            }
            nextSOR = getNextSlot(mainRack, mainSlot, front);
            if (mainRack.frontModules[mainSlot] != null) {
                isRoom = false;
            }
            if (isRoom) {
                mainRack.frontModules[mainSlot] = module;
                for (int i = 0; i < module.width - 1; i++) {
                    nextSOR.rack.frontModules[nextSOR.slot] = module;
                    nextSOR = getNextSlot(nextSOR.rack, nextSOR.slot, front);
                }
                this.setChanged();
                return true;
            } else {
                return false;
            }
        } else {
            for (int i = 0; i < module.width - 1; i++) {
                if (nextSOR.rack.backModules[nextSOR.slot - 4] != null) {
                    isRoom = false;
                }
                nextSOR = getNextSlot(nextSOR.rack, mainSlot, front);
            }
            nextSOR = getNextSlot(mainRack, mainSlot, front);
            if (mainRack.backModules[mainSlot - 4] != null) {
                isRoom = false;
            }
            if (isRoom) {
                mainRack.backModules[mainSlot - 4] = module;
                for (int i = 0; i < module.width - 1; i++) {
                    nextSOR.rack.backModules[nextSOR.slot - 4] = module;
                    nextSOR = getNextSlot(nextSOR.rack, nextSOR.slot, front);
                }
                this.setChanged();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean removeModule(Module module, ComputerRackBlockEntity rack, int slot, int generation) {
        if (generation >= 250 || module == null)
            return false; // just in case.
        SlotOnRack nextSOR;
        SlotOnRack prevSOR;

        if (slot >= 0 && slot <= 3 && frontModules[slot] == module) {
            nextSOR = getNextSlot(rack, slot, true);
            prevSOR = getPrevSlot(rack, slot, true);

            frontModules[slot] = null;
            nextSOR.rack.removeModule(module, nextSOR.rack, nextSOR.slot, generation + 1);
            prevSOR.rack.removeModule(module, prevSOR.rack, prevSOR.slot, generation + 1);
        } else if (slot >= 4 && slot <= 7 && backModules[slot - 4] == module) {
            nextSOR = getNextSlot(rack, slot, false);
            prevSOR = getPrevSlot(rack, slot, false);

            backModules[slot - 4] = null;
            nextSOR.rack.removeModule(module, nextSOR.rack, nextSOR.slot, generation + 1);
            prevSOR.rack.removeModule(module, prevSOR.rack, prevSOR.slot, generation + 1);
        }

        if (generation != 0) {
            return false;
        } else {
            this.setChanged();
            return true;
        }
    }

    public SlotOnRack getSlot(ComputerRackBlockEntity rack, BlockState state, BlockHitResult hitResult) {
        Vec2 frontCoords = BlockInteractionUtils.getFrontFaceCoords(state, hitResult);
        Direction hitFacing = hitResult.getDirection();
        Direction blockFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int frontSlot = (int) frontCoords.x / 4; // divide the area into four equal strips horizontally

        if (hitFacing == blockFacing) {
            return new SlotOnRack(rack, frontSlot); // the player hit one of the front slots, so we don't need to do
                                                    // anything else
            // after division
        } else if (hitFacing == blockFacing.getOpposite()) {
            return new SlotOnRack(rack, 7 - frontSlot); // the player wanted the back slots, but the back slots
                                                        // increment in reverse, we
            // can use the frontSlot value to move backwards from 7 to get it.
        } else {
            return new SlotOnRack(this, -1); // the player didn't hit either the back or front slots, return an invalid
                                             // slot.
        }
    }

    public SlotOnRack getNextSlot(ComputerRackBlockEntity rack, int slot, boolean front) {
        Direction nextFacing = this.getBlockState().getValue(ComputerRack.FACING).getCounterClockWise();
        Direction prevFacing = this.getBlockState().getValue(ComputerRack.FACING).getClockWise();
        BlockEntity nextBE;
        ComputerRackBlockEntity nextRack;

        if (rack == this && !(slot == 3 || slot == 7)) {
            return new SlotOnRack(this, slot + 1);
        } else {
            if (front) {
                nextBE = level.getBlockEntity(this.worldPosition.relative(nextFacing, 1));
                if (!(nextBE == null) && nextBE instanceof ComputerRackBlockEntity) {
                    nextRack = (ComputerRackBlockEntity) level
                            .getBlockEntity(this.worldPosition.relative(nextFacing, 1));
                } else {
                    return new SlotOnRack(this, -1);
                }
                return new SlotOnRack(nextRack, 0);

            } else {
                nextBE = level.getBlockEntity(this.worldPosition.relative(prevFacing, 1));
                if (!(nextBE == null) && nextBE instanceof ComputerRackBlockEntity) {
                    nextRack = (ComputerRackBlockEntity) level
                            .getBlockEntity(this.worldPosition.relative(prevFacing, 1));
                } else {
                    return new SlotOnRack(this, -1);
                }
                return new SlotOnRack(nextRack, 4);
            }
        }
    }

    public SlotOnRack getPrevSlot(ComputerRackBlockEntity rack, int slot, boolean front) {
        Direction nextFacing = this.getBlockState().getValue(ComputerRack.FACING).getCounterClockWise();
        Direction prevFacing = this.getBlockState().getValue(ComputerRack.FACING).getClockWise();
        BlockEntity prevBE;
        ComputerRackBlockEntity prevRack;

        if (rack == this && !(slot == 0 || slot == 4)) {
            return new SlotOnRack(this, slot - 1);
        } else {
            if (front) {
                prevBE = level.getBlockEntity(this.worldPosition.relative(prevFacing, 1));
                if (!(prevBE == null) && prevBE instanceof ComputerRackBlockEntity) {
                    prevRack = (ComputerRackBlockEntity) level
                            .getBlockEntity(this.worldPosition.relative(prevFacing, 1));
                } else {
                    return new SlotOnRack(this, -1);
                }
                return new SlotOnRack(prevRack, 3);

            } else {
                prevBE = level.getBlockEntity(this.worldPosition.relative(nextFacing, 1));
                if (!(prevBE == null) && prevBE instanceof ComputerRackBlockEntity) {
                    prevRack = (ComputerRackBlockEntity) level
                            .getBlockEntity(this.worldPosition.relative(nextFacing, 1));
                } else {
                    return new SlotOnRack(this, -1);
                }
                return new SlotOnRack(prevRack, 7);
            }
        }
    }

}
