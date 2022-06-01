/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Objects;


public class AutoAttack extends Module {

    public AutoAttack() {
        super("AutoAttack", "Automatically attacks the entity you're looking at", ModuleType.COMBAT);
    }

    @Override
    public void tick() {
        if (!(ShadowMain.client.crosshairTarget instanceof EntityHitResult) || Objects.requireNonNull(ShadowMain.client.player).getAttackCooldownProgress(0) < 1) {
            return;
        }
        Objects.requireNonNull(ShadowMain.client.interactionManager).attackEntity(ShadowMain.client.player, ((EntityHitResult) ShadowMain.client.crosshairTarget).getEntity());
        ShadowMain.client.player.swingHand(Hand.MAIN_HAND);
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

