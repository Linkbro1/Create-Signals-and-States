package net.linkbro.createsignalsandstates;

import net.linkbro.createsignalsandstates.block.SNSBlocks;
import net.linkbro.createsignalsandstates.blockentity.SNSBlockEntities;
import net.linkbro.createsignalsandstates.blockentity.renderer.ComputerRackBlockEntityRenderer;
import net.linkbro.createsignalsandstates.component.SNSDataComponents;
import net.linkbro.createsignalsandstates.item.SNSItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(CreateSignalsAndStates.MODID)
public class CreateSignalsAndStates {
    public static final String MODID = "createsignalsandstates";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public CreateSignalsAndStates(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        CREATIVE_MODE_TAB.register(modEventBus);

        SNSItems.register(modEventBus);
        SNSBlocks.register(modEventBus);
        SNSDataComponents.register(modEventBus);
        SNSBlockEntities.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static final Supplier<CreativeModeTab> SIGNALS_AND_STATES_TAB = CREATIVE_MODE_TAB.register("signals_and_states_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.createsignalsandstates.signals_and_states_tab"))
                    .icon(() -> new ItemStack(SNSItems.CABLE.get()))
                    .displayItems((ItemDisplayParameters,output) -> {
                        output.accept(SNSBlocks.COMPUTER_CONTROLLER);
                        output.accept(SNSBlocks.COMPUTER_RACK);

                        output.accept(SNSItems.CABLE);

                        output.accept(SNSItems.SUM_MODULE);
                        output.accept(SNSItems.SPLIT_MODULE);
                        output.accept(SNSItems.AND_MODULE);
                        output.accept(SNSItems.OR_MODULE);
                        output.accept(SNSItems.NOT_MODULE);
                        output.accept(SNSItems.BIAS_MODULE);
                        output.accept(SNSItems.LINK_MODULE);
                    }).build());

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @EventBusSubscriber(modid = CreateSignalsAndStates.MODID, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(SNSBlockEntities.RACK_BLOCK_ENTITY.get(), ComputerRackBlockEntityRenderer::new);
        }
    }
}
