/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Transitions;

public class Fullbright extends Module {

    double og;

    public Fullbright() {
        super("Fullbright", "Allows you to see in complete darkness", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        og = MathHelper.clamp(ShadowMain.client.options.gamma, 0, 1);
    }

    @Override
    public void disable() {
        ShadowMain.client.options.gamma = og;
    }

    @Override
    public void onFastTick() {
        ShadowMain.client.options.gamma = Transitions.transition(ShadowMain.client.options.gamma, 10, 300);
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
