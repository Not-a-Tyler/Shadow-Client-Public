/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.Rotations;

import java.util.Objects;

public class Spinner extends Module {

    final double r = 0;
    //    final SliderValue speed = (SliderValue) this.config.create("Timeout", 5, 0, 100, 0).description("How much to wait between rotations");
    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(5).name("Delay").description("How much to wait when spinning").min(0).max(100).precision(0).get());
    int timeout = 0;

    public Spinner() {
        super("Spinner", "Spins around like a maniac and throws whatever you have", ModuleType.MISC);
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {

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
    public void onFastTick() {
        timeout--; // decrease timeout
        if (timeout > 0) {
            return; // if timeout isn't expired, do nothing
        }
        timeout = (int) Math.floor(speed.getValue()); // timeout expired, set it back to full
        Rotations.setClientPitch((float) ((Math.random() * 60) - 30));
        Rotations.setClientYaw((float) (Math.random() * 360));
        PlayerInteractItemC2SPacket p = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND);
        Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(p);
        PlayerMoveC2SPacket p1 = new PlayerMoveC2SPacket.LookAndOnGround((float) r, Rotations.getClientPitch(), Objects.requireNonNull(ShadowMain.client.player).isOnGround());
        ShadowMain.client.getNetworkHandler().sendPacket(p1);
    }

    @Override
    public void onHudRender() {

    }
}

