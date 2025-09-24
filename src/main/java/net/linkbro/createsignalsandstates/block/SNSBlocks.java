package net.linkbro.createsignalsandstates.block;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.linkbro.createsignalsandstates.item.SNSItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SNSBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateSignalsAndStates.MODID);

    public static final DeferredBlock<ComputerController> COMPUTER_CONTROLLER = registerBlock("computer_controller",
            () -> new ComputerController(BlockBehaviour.Properties.of()
                    .strength(4f,6f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            ));

    public static final DeferredBlock<ComputerRack> COMPUTER_RACK = registerBlock("computer_rack",
            () -> new ComputerRack(BlockBehaviour.Properties.of()
                    .strength(4f,6f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        SNSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
