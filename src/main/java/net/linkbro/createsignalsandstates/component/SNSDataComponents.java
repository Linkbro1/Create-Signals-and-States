package net.linkbro.createsignalsandstates.component;

import net.linkbro.createsignalsandstates.CreateSignalsAndStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class SNSDataComponents {
        public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister
                        .createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateSignalsAndStates.MODID);

        public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> PARENT_SLOT_COORDS = register(
                        "parent_slot_coordinates", builder -> builder.persistent(BlockPos.CODEC));
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PARENT_SLOT = register(
                        "parent_slot", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT));
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MODULE_TYPE = register(
                        "module_type", builder -> builder.persistent(ExtraCodecs.NON_EMPTY_STRING));

        private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                        UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
                return DATA_COMPONENT_TYPES.register(name,
                                () -> builderOperator.apply(DataComponentType.builder()).build());
        }

        public static void register(IEventBus eventBus) {
                DATA_COMPONENT_TYPES.register(eventBus);
        }
}
