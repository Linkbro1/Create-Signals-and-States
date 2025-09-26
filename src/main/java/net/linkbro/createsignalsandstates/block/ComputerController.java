package net.linkbro.createsignalsandstates.block;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.linkbro.createsignalsandstates.blockentity.ComputerControllerBlockEntity;
import net.linkbro.createsignalsandstates.blockentity.SNSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ComputerController extends HorizontalKineticBlock implements IBE<ComputerControllerBlockEntity> {

    public ComputerController(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING,Direction.NORTH));
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
