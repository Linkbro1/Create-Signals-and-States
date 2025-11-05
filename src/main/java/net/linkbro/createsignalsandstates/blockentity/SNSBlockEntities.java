package net.linkbro.createsignalsandstates.blockentity;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.linkbro.createsignalsandstates.block.SNSBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SNSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CreateSignalsAndStates.MODID);

    @SuppressWarnings("null")
public static final Supplier<BlockEntityType<ComputerControllerBlockEntity>> CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("controller_block_entity", () -> BlockEntityType.Builder.of(
                    ComputerControllerBlockEntity::new, SNSBlocks.COMPUTER_CONTROLLER.get()).build(null));

    @SuppressWarnings("null")
public static final Supplier<BlockEntityType<ComputerRackBlockEntity>> RACK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("rack_block_entity", () -> BlockEntityType.Builder.of(
                    ComputerRackBlockEntity::new, SNSBlocks.COMPUTER_RACK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
