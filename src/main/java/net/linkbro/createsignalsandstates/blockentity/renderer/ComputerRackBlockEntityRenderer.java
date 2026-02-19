package net.linkbro.createsignalsandstates.blockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;
import net.linkbro.createsignalsandstates.classes.Module;
import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.linkbro.createsignalsandstates.util.linkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ComputerRackBlockEntityRenderer implements BlockEntityRenderer<ComputerRackBlockEntity> {
    public ComputerRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    private double pixel = linkUtils.VoxelLength;

    @Override
    public void render(ComputerRackBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = blockEntity.getLevel();
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos frontPos = blockEntity.getBlockPos().relative(facing, 1);
        BlockPos backPos = blockEntity.getBlockPos().relative(facing.getOpposite(), 1);

        int light;
        double baseOffset = 6 * pixel;

        for (int i = 0; i < blockEntity.Modules.length; i++) { // POTENTIAL REGRESSION: THIS MIGHT NO LONGER BE ABLE TO
                                                               // HANDLE MODULES WITH MORE THAN 1 WIDTH
            SlotOnRack currentSOR = new SlotOnRack(blockEntity, i);

            if (linkUtils.isModuleOrigin(currentSOR)) {
                Module module = blockEntity.Modules[i];
                ItemStack slotStack = ModuleFactory.ItemStackFromModule(module);
                boolean front = currentSOR.slot >= 0 && currentSOR.slot <= 3;

                double slotOffset;
                double widthOffset = module == null ? 0 : (2 * pixel) * (module.width - 1);
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                if (front) {
                    linkUtils.applyFacingRotation(poseStack, facing);
                    slotOffset = (4 * pixel) * i;
                    light = linkUtils.lightAtPos(level, frontPos);
                } else {
                    linkUtils.applyFacingRotation(poseStack, facing.getOpposite());
                    slotOffset = (4 * pixel) * (i - 4);
                    light = linkUtils.lightAtPos(level, backPos);
                }
                poseStack.translate(baseOffset - slotOffset - widthOffset, 0, -8.5 * pixel);
                itemRenderer.renderStatic(slotStack, ItemDisplayContext.FIXED, light, packedOverlay, poseStack,
                        bufferSource, level, 1);
                poseStack.popPose();
            }
        }
    }

}
