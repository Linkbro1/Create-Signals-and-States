package net.linkbro.createsignalsandstates.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ComputerRackBlockEntity extends BlockEntity {
    public ComputerRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(SNSBlockEntities.RACK_BLOCK_ENTITY.get(), pos, blockState);
    }
}
