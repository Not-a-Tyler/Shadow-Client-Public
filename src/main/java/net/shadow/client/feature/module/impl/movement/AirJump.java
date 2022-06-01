/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump", "Allows you to jump mid air", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
            return;
        }
        if (ShadowMain.client.options.jumpKey.isPressed()) {
            ShadowMain.client.player.setOnGround(true);
            ShadowMain.client.player.fallDistance = 0f;
        }
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
