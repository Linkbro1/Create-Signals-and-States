package net.linkbro.createsignalsandstates.block;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.linkbro.createsignalsandstates.blockentity.ComputerControllerBlockEntity;
import net.linkbro.createsignalsandstates.blockentity.SNSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ComputerController extends HorizontalKineticBlock implements IBE<ComputerControllerBlockEntity> {

    public ComputerController(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING,Direction.NORTH));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof ComputerControllerBlockEntity controller) {
            if (!player.isShiftKeyDown()) {
                controller.handleRightClick(stack, state, level, pos, player, hand, hitResult);
                return ItemInteractionResult.SUCCESS;
            } else {
                controller.handleShiftRightClick(stack, state, level, pos, player, hand, hitResult);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Class<ComputerControllerBlockEntity> getBlockEntityClass() {
        return ComputerControllerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ComputerControllerBlockEntity> getBlockEntityType() {
        return SNSBlockEntities.CONTROLLER_BLOCK_ENTITY.get();
    }
}
