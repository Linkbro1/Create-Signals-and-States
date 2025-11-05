package net.linkbro.createsignalsandstates.blockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;
import net.linkbro.createsignalsandstates.util.BlockInteractionUtils;
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

public class ComputerRackBlockEntityRenderer implements BlockEntityRenderer<ComputerRackBlockEntity>{
    public ComputerRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ComputerRackBlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = blockEntity.getLevel();
        Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos frontPos = blockEntity.getBlockPos().relative(facing, 1);
        BlockPos backPos = blockEntity.getBlockPos().relative(facing.getOpposite(), 1);

        int frontLight = BlockInteractionUtils.lightAtPos(level, frontPos);
        int backLight = BlockInteractionUtils.lightAtPos(level, backPos);

        for (int i = 0; i < 4; i++) {
            ItemStack slotStack = blockEntity.inventory.getStackInSlot(i);

            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            BlockInteractionUtils.applyFacingRotation(poseStack,facing);
            poseStack.translate(0.375-(0.25*i), 0, -0.53125); //these magic numbers are derived from 0.0625 = a pixel, 
            itemRenderer.renderStatic(slotStack, ItemDisplayContext.FIXED, frontLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        for (int i = 4; i < 8; i++) {
            ItemStack slotStack = blockEntity.inventory.getStackInSlot(i);

            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            BlockInteractionUtils.applyFacingRotation(poseStack,facing);
            poseStack.translate(-0.375+(0.25*(i-4)), 0, 0.53125);
            poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            itemRenderer.renderStatic(slotStack, ItemDisplayContext.FIXED, backLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }

    }


}
