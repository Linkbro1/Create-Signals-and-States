package net.linkbro.createsignalsandstates.util;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class BlockInteractionUtils {
    public static Vec3 getPixelCoordsFromHit(BlockHitResult hitResult) {
        Vec3 hitPos = hitResult.getLocation();
        BlockPos blockPos = hitResult.getBlockPos();

        double relX = hitPos.x - blockPos.getX();
        double relY = hitPos.y - blockPos.getY();
        double relZ = hitPos.z - blockPos.getZ();

        double pixelX = (int)(16*relX);
        double pixelY = (int)(16*relY);
        double pixelZ = (int)(16*relZ);

        return new Vec3(pixelX,pixelY,pixelZ);
    }

    public static Vec2 getFrontFaceCoords(BlockState state, BlockHitResult hitResult) {
        Vec3 pixelCoords = BlockInteractionUtils.getPixelCoordsFromHit(hitResult);
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();

        switch (facing) {
            case NORTH -> {return new Vec2((float)(pixelCoords.x),(float)pixelCoords.y);}
            case EAST -> {return new Vec2((float)(pixelCoords.z),(float)pixelCoords.y);}
            case SOUTH -> {return new Vec2((float)(15-(pixelCoords.x)),(float)pixelCoords.y);}
            case WEST -> {return new Vec2((float)(15-(pixelCoords.z)),(float)pixelCoords.y);}
            case null, default -> {return new Vec2(0,0);}
        }
    }

    public static int lightAtPos(Level level, BlockPos pos) {
        return LightTexture.pack( level.getBrightness(LightLayer.BLOCK, pos) , level.getBrightness(LightLayer.SKY, pos));
    }
}
