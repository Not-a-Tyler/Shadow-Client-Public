/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Objects;

public class EdgeJump extends Module {

    public EdgeJump() {
        super("EdgeJump", "Jumps automatically at the edges of blocks", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ShadowMain.client.player == null || ShadowMain.client.world == null) {
            return;
        }
        if (!ShadowMain.client.player.isOnGround() || ShadowMain.client.player.isSneaking()) {
            return;
        }

        Box bounding = ShadowMain.client.player.getBoundingBox();
        bounding = bounding.offset(0, -0.5, 0);
        bounding = bounding.expand(-0.001, 0, -0.001);
        if (!ShadowMain.client.world.getBlockCollisions(client.player, bounding).iterator().hasNext()) {
            Objects.requireNonNull(client.player).jump();
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
