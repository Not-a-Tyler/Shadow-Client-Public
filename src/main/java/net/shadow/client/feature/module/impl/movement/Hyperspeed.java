/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class Hyperspeed extends Module {

    public final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(3).name("Speed").description("The speed multiplier to apply").min(1).max(10).precision(3).get());

    public Hyperspeed() {
        super("Hyperspeed", "Gives you an extreme speed boost", ModuleType.MOVEMENT);
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
    public void onHudRender() {

    }
}
