package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.util.BlockInteractionUtils;
import net.linkbro.createsignalsandstates.util.SNSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ComputerRackBlockEntity extends BlockEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        };
    };

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
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
        ItemStack slotStack = inventory.extractItem(slot, 1, true);

            if (!stack.isEmpty() && stack.is(SNSTags.Items.MODULES) && slotStack == ItemStack.EMPTY) {
                inventory.insertItem(slot, stack.copy(), false);
                stack.shrink(1);
            } else if (stack.isEmpty()) {
                player.setItemInHand(hand, slotStack);
                inventory.setStackInSlot(slot, ItemStack.EMPTY);
            }


        player.displayClientMessage(Component.literal("slot hit: " + slot),true);
    }

    public void handleShiftRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    }
}
