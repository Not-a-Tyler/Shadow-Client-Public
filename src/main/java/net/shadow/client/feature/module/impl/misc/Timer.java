/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;

public class Timer extends Module {

    //    final SliderValue newTps = this.config.create("New TPS", 20, 0.1, 100, 1);
    final DoubleSetting newTps = this.config.create(new DoubleSetting.Builder(20).name("New TPS").description("To what to set the new tps to").min(0.1).max(100).precision(1).get());

    public Timer() {
        super("Timer", "Changes the speed of the game client side", ModuleType.MISC);
    }

    @Override
    public void tick() {
        Utils.setClientTps((float) (newTps.getValue() + 0d));
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        Utils.setClientTps(20f);
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

