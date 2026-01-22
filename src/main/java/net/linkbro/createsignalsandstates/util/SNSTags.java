package net.linkbro.createsignalsandstates.util;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SNSTags {
    public static class Blocks {
        public static TagKey<Block> COMPUTERBLOCKS = createTag("computer_blocks");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(CreateSignalsAndStates.MODID, name));
        }
    }

    public static class Items {
        public static TagKey<Item> MODULES = createTag("modules");
        public static TagKey<Item> ONE_SLOT_MODULES = createTag("one_slot_wide_modules");
        public static TagKey<Item> TWO_SLOT_MODULES = createTag("two_slot_wide_modules");
        public static TagKey<Item> THREE_SLOT_MODULES = createTag("three_slot_wide_modules");
        public static TagKey<Item> FOUR_SLOT_MODULES = createTag("four_slot_wide_modules");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CreateSignalsAndStates.MODID, name));
        }
    }
}
