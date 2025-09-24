package net.linkbro.createsignalsandstates.item;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SNSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateSignalsAndStates.MODID);

    public static final DeferredItem<Item> CABLE = ITEMS.register("cable",
            () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> SUM_MODULE = ITEMS.register("sum_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SPLIT_MODULE = ITEMS.register("split_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AND_MODULE = ITEMS.register("and_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> OR_MODULE = ITEMS.register("or_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> NOT_MODULE = ITEMS.register("not_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BIAS_MODULE = ITEMS.register("bias_module",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LINK_MODULE = ITEMS.register("link_module",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
