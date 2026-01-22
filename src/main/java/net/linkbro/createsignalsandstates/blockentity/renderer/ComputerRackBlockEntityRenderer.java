package net.linkbro.createsignalsandstates.blockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;
import net.linkbro.createsignalsandstates.classes.ModuleFactory;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
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

public class ComputerRackBlockEntityRenderer implements BlockEntityRenderer<ComputerRackBlockEntity> {
    public ComputerRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    private double pixel = BlockInteractionUtils.VoxelLength;

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
        double baseOffset = 6 * pixel;

        if (blockEntity.frontModules.length >= 3) {
            for (int i = 0; i < 4; i++) {
                SlotOnRack previousSOR = blockEntity.getPrevSlot(blockEntity, i, true);
                net.linkbro.createsignalsandstates.classes.Module module = blockEntity.frontModules[i];

                // assume we should render nothing, then if the previous Block wasn't another
                // rack, or if it was a rack and the slot isn't occupied by an earlier instance,
                // change that assumption to the relevant itemstack.
                ItemStack slotStack = ItemStack.EMPTY;
                if ((previousSOR.rack == blockEntity && previousSOR.slot == -1)
                        || (previousSOR.slot != -1 && previousSOR.rack.frontModules[previousSOR.slot] != module)) {
                    slotStack = ModuleFactory.ItemStackFromModule(module);
                }

                double slotOffset = (4 * pixel) * i;
                double widthOffset = module != null ? (2 * pixel) * (module.width - 1) : 0;

                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                BlockInteractionUtils.applyFacingRotation(poseStack, facing);
                poseStack.translate(baseOffset - slotOffset - widthOffset, 0, -8.5 * pixel);
                itemRenderer.renderStatic(slotStack, ItemDisplayContext.FIXED, frontLight, packedOverlay, poseStack,
                        bufferSource, blockEntity.getLevel(), 1);
                poseStack.popPose();
            }
        }

        if (blockEntity.backModules.length >= 3) {
            for (int i = 0; i < 4; i++) {
                if (blockEntity.backModules.length < 4) {
                    break;
                }
                SlotOnRack previousSOR = blockEntity.getPrevSlot(blockEntity, i + 4, false);
                net.linkbro.createsignalsandstates.classes.Module module = blockEntity.backModules[i];

                // ItemStack slotStack = ItemStack.EMPTY;
                // if ((previousSOR.rack == blockEntity && previousSOR.slot == -1)
                // || (previousSOR.slot != -1 && previousSOR.rack.frontModules[previousSOR.slot]
                // != module)) {
                // slotStack = ModuleFactory.ItemStackFromModule(module);
                // }
                ItemStack slotStack = ItemStack.EMPTY;
                if ((previousSOR.rack == blockEntity && previousSOR.slot == -1)
                        || (previousSOR.slot != -1 && previousSOR.rack.backModules[previousSOR.slot - 4] != module)) {
                    slotStack = ModuleFactory.ItemStackFromModule(module);
                }

                // if (i - 1 < 0) {
                // slotStack = ModuleFactory.ItemStackFromModule(module);
                // } else if (blockEntity.backModules[i - 1] != module) {
                // slotStack = ModuleFactory.ItemStackFromModule(module);
                // }

                double slotOffset = (4 * pixel) * i;
                double widthOffset = module != null ? (2 * pixel) * (module.width - 1) : 0;

                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                BlockInteractionUtils.applyFacingRotation(poseStack, facing);
                poseStack.translate(-baseOffset + slotOffset + widthOffset, 0, 8.5 * pixel);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
                itemRenderer.renderStatic(slotStack, ItemDisplayContext.FIXED, backLight, packedOverlay, poseStack,
                        bufferSource, blockEntity.getLevel(), 1);
                poseStack.popPose();
            }
        }
    }

}
