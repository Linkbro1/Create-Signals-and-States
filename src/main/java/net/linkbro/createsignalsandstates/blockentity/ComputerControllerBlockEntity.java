package net.linkbro.createsignalsandstates.blockentity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.linkbro.createsignalsandstates.util.BlockInteractionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;

public class ComputerControllerBlockEntity extends KineticBlockEntity {
    public ComputerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(SNSBlockEntities.CONTROLLER_BLOCK_ENTITY.get(), pos, state);
    }

    private boolean isRunning = false;
    private final float worldTicksPerComputerTickMax = 20;
    private float worldTicksPerComputerTick = 20;
    private float tickTimer = 20;
    private Player lastPlayer;

    @Override
    public void tick() {
        super.tick();
        if (isRunning) {
            tickTimer -= 1;
            if (tickTimer <= 0) {
                computerTick();
                tickTimer = worldTicksPerComputerTick;
            }
        }
    }

    private boolean tock = false;
    public void computerTick() {
        if (!tock) {
            tickNoise(0.01f,1.25f);
        } else {
            tickNoise(0.01f,0.75f);
        }
        tock = !tock;
    }

    public void playButton() {
        tickTimer = worldTicksPerComputerTick;
        isRunning = !isRunning;
        lastPlayer.displayClientMessage(Component.literal(isRunning ? "play" : "pause"),true);
        tickNoise(1f,1f);
    }

     public void stepButton() { // i never knew my real button...
        computerTick();
        lastPlayer.displayClientMessage(Component.literal("Step"),true);
        tickNoise(1f, 1.1f);
     }

     public void speedSlider(float value) {
        float sliderValue = ((value-2)/11)*1;//some maths to make the 2-13 range into a 0-1 range
        worldTicksPerComputerTick = worldTicksPerComputerTickMax * (1 - sliderValue);
        lastPlayer.displayClientMessage(Component.literal("world ticks per computer tick: " + worldTicksPerComputerTick),true);
     }

     public void tickNoise(float volume, float pitch) {
         assert level != null;
         level.playSound(lastPlayer, this.getBlockPos(), SoundEvents.DISPENSER_DISPENSE,SoundSource.BLOCKS, volume, pitch);
     }

    public void handleRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        Vec2 faceCoords =  BlockInteractionUtils.getFrontFaceCoords(state, hitResult);
        Direction hitFacing = hitResult.getDirection();
        Direction blockFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        lastPlayer = player;
        if (hitFacing != blockFacing) return;

        if (faceCoords.y >= 2 && faceCoords.y <= 8) {
            if (faceCoords.x >= 2 && faceCoords.x <= 6) {
                playButton();
            } else if (faceCoords.x >= 8 && faceCoords.x <= 13) {
                stepButton();
            }
        } else if (faceCoords.y >= 10 && faceCoords.y <= 13) {
            if (faceCoords.x >= 2 && faceCoords.x <= 13) {
                speedSlider(faceCoords.x);
            }
        }
    }

    public void handleShiftRightClick(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult){
        lastPlayer = player;
    }

    @Override
    public float calculateStressApplied() {
        return 4.0f;
    }
}
