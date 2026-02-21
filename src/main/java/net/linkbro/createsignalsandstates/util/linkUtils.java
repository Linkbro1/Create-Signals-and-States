package net.linkbro.createsignalsandstates.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.linkbro.createsignalsandstates.block.ComputerRack;
import net.linkbro.createsignalsandstates.blockentity.ComputerRackBlockEntity;
import net.linkbro.createsignalsandstates.classes.SlotOnRack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class linkUtils {

    public static double VoxelLength = 0.0625;

    public static Vec3 getPixelCoordsFromHit(BlockHitResult hitResult) {
        Vec3 hitPos = hitResult.getLocation();
        BlockPos blockPos = hitResult.getBlockPos();

        double relX = hitPos.x - blockPos.getX();
        double relY = hitPos.y - blockPos.getY();
        double relZ = hitPos.z - blockPos.getZ();

        double pixelX = (int) (16 * relX);
        double pixelY = (int) (16 * relY);
        double pixelZ = (int) (16 * relZ);

        return new Vec3(pixelX, pixelY, pixelZ);
    }

    public static Vec2 getFrontFaceCoords(BlockState state, BlockHitResult hitResult) {
        Vec3 pixelCoords = linkUtils.getPixelCoordsFromHit(hitResult);
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();

        switch (facing) {
            case NORTH -> {
                return new Vec2((float) (pixelCoords.x), (float) pixelCoords.y);
            }
            case EAST -> {
                return new Vec2((float) (pixelCoords.z), (float) pixelCoords.y);
            }
            case SOUTH -> {
                return new Vec2((float) (15 - (pixelCoords.x)), (float) pixelCoords.y);
            }
            case WEST -> {
                return new Vec2((float) (15 - (pixelCoords.z)), (float) pixelCoords.y);
            }
            case null, default -> {
                return new Vec2(0, 0);
            }
        }
    }

    public static void applyFacingRotation(PoseStack poseStack, Direction facing) {
        // this requires that the pose stack be at 0.5, 0.5, 0.5.
        switch (facing) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case null, default -> poseStack.mulPose(Axis.YP.rotationDegrees(0));
        }
    }

    public static int lightAtPos(Level level, BlockPos pos) {
        return LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos), level.getBrightness(LightLayer.SKY, pos));
    }

    public static boolean isModuleOrigin(SlotOnRack SOR) {
        if (SOR == null) {
            return false;
        }
        SlotOnRack previousSOR = getPreviousSlotOnRack(SOR);

        if (previousSOR == null
                || (previousSOR.rack.Modules[previousSOR.slot] != SOR.rack.Modules[SOR.slot])) {
            return true;
        } else {
            return false;
        }
    }

    public static SlotOnRack getSlotOnRack(ComputerRackBlockEntity rack, BlockState state, BlockHitResult hitResult) {
        Vec2 frontCoords = linkUtils.getFrontFaceCoords(state, hitResult);
        Direction hitFacing = hitResult.getDirection();
        Direction blockFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        int frontSlot = (int) frontCoords.x / 4; // divide the area into four equal strips horizontally

        if (hitFacing == blockFacing) {
            return new SlotOnRack(rack, frontSlot); // the player hit one of the front slots, so we don't need to do
                                                    // anything else
            // after division
        } else if (hitFacing == blockFacing.getOpposite()) {
            return new SlotOnRack(rack, 7 - frontSlot); // the player wanted the back slots, but the back slots
                                                        // increment in reverse, we
            // can use the frontSlot value to move backwards from 7 to get it.
        } else {
            return new SlotOnRack(rack, -1); // the player didn't hit either the back or front slots, return an invalid
                                             // slot.
        }
    }

    public static SlotOnRack getNextSlotOnRack(SlotOnRack slotOnRack) {
        Direction nextFacing = slotOnRack.rack.getBlockState().getValue(ComputerRack.FACING).getCounterClockWise();
        Direction prevFacing = slotOnRack.rack.getBlockState().getValue(ComputerRack.FACING).getClockWise();
        ComputerRackBlockEntity rack = slotOnRack.rack;
        int slot = slotOnRack.slot;
        BlockEntity nextBE;
        ComputerRackBlockEntity nextRack;
        Boolean front = slot >= 0 && slot <= 3;
        Level level = rack.getLevel();

        if (!(slot == 3 || slot == 7)) {
            return new SlotOnRack(rack, slot + 1);
        } else {
            if (front) {
                nextBE = level.getBlockEntity(rack.getBlockPos().relative(nextFacing, 1));
                if (nextBE != null && nextBE instanceof ComputerRackBlockEntity) {
                    nextRack = (ComputerRackBlockEntity) nextBE;
                } else {
                    return null;
                }
                return new SlotOnRack(nextRack, 0);

            } else {
                nextBE = level.getBlockEntity(rack.getBlockPos().relative(prevFacing, 1));
                if (!(nextBE == null) && nextBE instanceof ComputerRackBlockEntity) {
                    nextRack = (ComputerRackBlockEntity) nextBE;
                } else {
                    return null;
                }
                return new SlotOnRack(nextRack, 4);
            }
        }
    }

    public static SlotOnRack getPreviousSlotOnRack(SlotOnRack slotOnRack) {
        Direction nextFacing = slotOnRack.rack.getBlockState().getValue(ComputerRack.FACING).getCounterClockWise();
        Direction prevFacing = slotOnRack.rack.getBlockState().getValue(ComputerRack.FACING).getClockWise();
        ComputerRackBlockEntity rack = slotOnRack.rack;
        int slot = slotOnRack.slot;
        BlockEntity prevBE;
        ComputerRackBlockEntity prevRack;
        Boolean front = slot >= 0 && slot <= 3;
        Level level = rack.getLevel();

        if (!(slot == 0 || slot == 4)) {
            return new SlotOnRack(slotOnRack.rack, slot - 1);
        } else {
            if (front) {
                prevBE = level.getBlockEntity(rack.getBlockPos().relative(prevFacing, 1));
                if (!(prevBE == null) && prevBE instanceof ComputerRackBlockEntity) {
                    prevRack = (ComputerRackBlockEntity) prevBE;
                } else {
                    return null;
                }
                return new SlotOnRack(prevRack, 3);

            } else {
                prevBE = level.getBlockEntity(rack.getBlockPos().relative(nextFacing, 1));
                if (!(prevBE == null) && prevBE instanceof ComputerRackBlockEntity) {
                    prevRack = (ComputerRackBlockEntity) prevBE;
                } else {
                    return null;
                }
                return new SlotOnRack(prevRack, 7);
            }
        }
    }
}
