/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Objects;

public class Step extends Module {

    //    final SliderValue height = (SliderValue) this.config.create("Step height", 3, 1, 50, 0).description("How high to step");
    final DoubleSetting height = this.config.create(new DoubleSetting.Builder(3).name("Height").description("How high to step").min(1).max(50).precision(0).get());

    public Step() {
        super("Step", "Allows you to step up full blocks", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
            return;
        }
        ShadowMain.client.player.stepHeight = (float) (height.getValue() + 0);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
            return;
        }
        Objects.requireNonNull(client.player).stepHeight = 0.6f;
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
}

