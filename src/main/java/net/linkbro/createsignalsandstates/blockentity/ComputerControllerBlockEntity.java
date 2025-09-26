package net.linkbro.createsignalsandstates.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ComputerControllerBlockEntity extends KineticBlockEntity {
    public ComputerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(SNSBlockEntities.CONTROLLER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public float calculateStressApplied() {
        return 4.0f;
    }
}
