package net.linkbro.createsignalsandstates.block;

import com.mojang.serialization.MapCodec;
import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;
import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.linkbro.createsignalsandstates.util.SNSTags;
import net.linkbro.createsignalsandstates.util.linkUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ComputerRack extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ComputerRack(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ComputerRackBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ComputerRackBlockEntity controller) {
            SlotOnRack targetedSOR = linkUtils.getSlotOnRack(controller, state, hitResult);
            if (player.isCrouching() && stack.isEmpty()) {
                ItemStack moduleStack = ModuleFactory.ItemStackFromModule(targetedSOR.rack.Modules[targetedSOR.slot]);
                if (controller.removeModule(targetedSOR)) {
                    level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, moduleStack);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            } else if (stack.is(SNSTags.Items.MODULES)) {
                if (controller.addModule(ModuleFactory.moduleFromItemstack(stack), targetedSOR)) {
                    level.playSound(player, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return ItemInteractionResult.SUCCESS;
            } else {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
