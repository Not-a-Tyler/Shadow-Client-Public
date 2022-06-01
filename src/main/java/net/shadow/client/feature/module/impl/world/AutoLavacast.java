/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;

import net.minecraft.block.Block;
import net.minecraft.client.input.Input;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.Rotations;
import net.shadow.client.helper.Timer;
import net.shadow.client.helper.render.Renderer;

import java.awt.Color;

public class AutoLavacast extends Module {

    static boolean moveForwards = false;
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Bypass).name("Mode").description("How to place and move. Bypass is slow but looks legit, fast is VERY speedy").get());
    final Timer timer = new Timer();
    Input original;
    Vec3i incr;
    BlockPos start;

    public AutoLavacast() {
        super("AutoLavacast", "Makes a lavacast", ModuleType.WORLD);
        mode.showIf(() -> !this.isEnabled()); // only show when disabled to prevent changes mid action
    }

    BlockPos getNextPosition() {
        int y = 0;
        while ((y + start.getY()) < ShadowMain.client.world.getTopY()) {
            Vec3i ie = incr.multiply(y + 1);
            BlockPos next = start.add(ie).add(0, y, 0);
            if (ShadowMain.client.world.getBlockState(next).getMaterial().isReplaceable()) {
                return next;
            }
            y++;
        }
        return null;
    }

    @Override
    public void onFastTick() {
        if (mode.getValue() == Mode.Fast && !timer.hasExpired(100)) {
            return;
        }
        timer.reset();
        BlockPos next = getNextPosition();
        if (next == null) {
            setEnabled(false);
            return;
        }
        Vec3d placeCenter = Vec3d.of(next).add(.5, .5, .5);
        if (mode.getValue() == Mode.Bypass) {
            Rotations.lookAtPositionSmooth(placeCenter, 6);
            if (((ShadowMain.client.player.horizontalCollision && moveForwards) || ShadowMain.client.player.getBoundingBox().intersects(Vec3d.of(next), Vec3d.of(next).add(1, 1, 1))) && ShadowMain.client.player.isOnGround()) {
                ShadowMain.client.player.jump();
                ShadowMain.client.player.setOnGround(false);
            }
        }

        if (placeCenter.distanceTo(ShadowMain.client.player.getCameraPosVec(1)) < ShadowMain.client.interactionManager.getReachDistance()) {
            moveForwards = false;

            ItemStack is = ShadowMain.client.player.getInventory().getMainHandStack();
            if (is.isEmpty()) {
                return;
            }
            if (is.getItem() instanceof BlockItem bi) {
                Block p = bi.getBlock();
                if (p.getDefaultState().canPlaceAt(ShadowMain.client.world, next)) {
                    ShadowMain.client.execute(() -> {
                        BlockHitResult bhr = new BlockHitResult(placeCenter, Direction.DOWN, next, false);
                        ShadowMain.client.interactionManager.interactBlock(ShadowMain.client.player, ShadowMain.client.world, Hand.MAIN_HAND, bhr);
                        if (mode.getValue() == Mode.Fast) {
                            Vec3d goP = Vec3d.of(next).add(0.5, 1.05, 0.5);
                            ShadowMain.client.player.updatePosition(goP.x, goP.y, goP.z);
                        }
                    });
                }
            }

        } else {
            moveForwards = true;
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if (original == null) {
            original = ShadowMain.client.player.input;
        }
        if (mode.getValue() == Mode.Bypass) {
            ShadowMain.client.player.input = new ListenInput();
        }
        incr = ShadowMain.client.player.getMovementDirection().getVector();
        start = ShadowMain.client.player.getBlockPos();
    }

    @Override
    public void disable() {
        ShadowMain.client.player.input = original;
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        BlockPos next = getNextPosition();
        Renderer.R3D.renderOutline(Vec3d.of(start), new Vec3d(1, 0.01, 1), Color.RED, matrices);
        if (next != null) {
            Renderer.R3D.renderOutline(Vec3d.of(next), new Vec3d(1, 1, 1), Color.BLUE, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }

    public enum Mode {
        Bypass, Fast
    }

    static class ListenInput extends Input {
        @Override
        public void tick(boolean slowDown) {
            this.movementForward = moveForwards ? 1 : 0;
        }
    }
}
