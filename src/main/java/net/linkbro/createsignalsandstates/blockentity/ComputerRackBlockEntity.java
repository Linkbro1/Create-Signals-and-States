package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.abstractclasses.Module;
import net.linkbro.createsignalsandstates.abstractclasses.ModuleFactory;
import net.linkbro.createsignalsandstates.util.BlockInteractionUtils;
import net.linkbro.createsignalsandstates.util.SNSTags;
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
    public net.linkbro.createsignalsandstates.abstractclasses.Module[] frontModules = new Module[4];
    public net.linkbro.createsignalsandstates.abstractclasses.Module[] backModules = new Module[4];

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag frontModuleList = new ListTag();
        for (net.linkbro.createsignalsandstates.abstractclasses.Module module : frontModules) {
            if (module != null) {
                frontModuleList.add(net.linkbro.createsignalsandstates.abstractclasses.Module.serializeNBT(module));
            } else {
                CompoundTag emptyTag = new CompoundTag();
                frontModuleList.add(emptyTag);
            }
        }
        tag.put("frontModuleList", frontModuleList);

        ListTag backModuleList = new ListTag();
        for (net.linkbro.createsignalsandstates.abstractclasses.Module module : backModules) {
            if (module != null) {
                backModuleList.add(net.linkbro.createsignalsandstates.abstractclasses.Module.serializeNBT(module));
            } else {
                CompoundTag emptyTag = new CompoundTag();
                backModuleList.add(emptyTag);
            }
        }
        tag.put("backModuleList", backModuleList);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);

        ListTag frontModuleList = tag.getList("frontModuleList", Tag.TAG_COMPOUND);
        for (int i = 0; i < frontModuleList.size(); i++) {
            CompoundTag moduleTag = frontModuleList.getCompound(i);
            this.frontModules[i] = Module.deserializeNBT(moduleTag);
        }
 
        
        ListTag backModuleList = tag.getList("backModuleList", Tag.TAG_COMPOUND);
        for (int i = 0; i < backModuleList.size(); i++) {
            CompoundTag moduleTag = backModuleList.getCompound(i);
            this.backModules[i] = Module.deserializeNBT(moduleTag);
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

    public int getSlot(BlockState state, BlockHitResult hitResult) {
        Vec2 frontCoords = BlockInteractionUtils.getFrontFaceCoords(state, hitResult);
        Direction hitFacing = hitResult.getDirection();
        Direction blockFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int frontSlot = (int)frontCoords.x/4; // divide the area into four equal strips horizontally

        if (hitFacing == blockFacing) {
            return frontSlot; // the player hit one of the front slots, so we don't need to do anything else after division
        } else if (hitFacing == blockFacing.getOpposite()) {
            return 7-frontSlot; // the player wanted the back slots, but the back slots increment in reverse, we can use the frontSlot value to move backwards from 7 to get it.
        } else {
            return -1; // the player didn't hit either the back or front slots, return an invalid slot.
        }
    }

    public void handleRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int slot = getSlot(state,hitResult);
        if (slot == -1) return; // didn't hit either the back or the front.

        if (!stack.isEmpty() && stack.is(SNSTags.Items.MODULES)) {
            //TODO: handle modules that are more than one slot wide
            if (slot <= 3 && frontModules[slot] == null) {
                frontModules[slot] = ModuleFactory.moduleFromItemstack(stack);
                stack.shrink(1);
            } else if (backModules[slot-4] == null) {
                backModules[slot-4] = ModuleFactory.moduleFromItemstack(stack);
                stack.shrink(1);
            }
            this.setChanged();
        }
            //player.displayClientMessage(Component.literal("slot hit: " + slot),true);
    }

    public void handleShiftRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int slot = getSlot(state, hitResult);
        if (slot == -1) return;
        ItemStack returnItem = ItemStack.EMPTY;

        if (stack.isEmpty()) {
            if (slot <= 3) {
                returnItem = ModuleFactory.ItemStackFromModule(frontModules[slot]);
                frontModules[slot] = null;
            } else{
                returnItem = ModuleFactory.ItemStackFromModule(backModules[slot-4]);
                backModules[slot-4] = null;
            }
            player.setItemInHand(hand, returnItem);
            this.setChanged();
        }
    }
}
