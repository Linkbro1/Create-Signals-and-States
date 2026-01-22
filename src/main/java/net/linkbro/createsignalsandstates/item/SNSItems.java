package net.linkbro.createsignalsandstates.item;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.linkbro.createsignalsandstates.component.SNSDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SNSItems {
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateSignalsAndStates.MODID);

        public static final DeferredItem<Item> CABLE = ITEMS.register("cable", () -> new Item(new Item.Properties()));

        public static final DeferredItem<Item> DUMMY_MODULE = ITEMS.register("dummy_module",
                        () -> new Item(new Item.Properties()
                                        .component(SNSDataComponents.PARENT_SLOT_COORDS, new BlockPos(0, 0, 0))
                                        .component(SNSDataComponents.PARENT_SLOT, -1)));

        public static final DeferredItem<Item> SUM_MODULE = ITEMS.register("sum_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "SUM")));

        public static final DeferredItem<Item> SPLIT_MODULE = ITEMS.register("split_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "SPLIT")));

        public static final DeferredItem<Item> AND_MODULE = ITEMS.register("and_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "AND")));

        public static final DeferredItem<Item> OR_MODULE = ITEMS.register("or_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "OR")));

        public static final DeferredItem<Item> NOT_MODULE = ITEMS.register("not_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "NOT")));

        public static final DeferredItem<Item> BIAS_MODULE = ITEMS.register("bias_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "BIAS")));

        public static final DeferredItem<Item> LINK_MODULE = ITEMS.register("link_module",
                        () -> new Item(new Item.Properties().component(SNSDataComponents.MODULE_TYPE, "LINK")));

        public static void register(IEventBus eventBus) {
                ITEMS.register(eventBus);
        }
}
