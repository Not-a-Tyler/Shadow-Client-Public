/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Objects;

public class ShulkerDeflector extends Module {
    final BooleanSetting checkOwner = this.config.create(new BooleanSetting.Builder(true).name("Check owner").description("Check if you own the projectile, else hit it").get());

    public ShulkerDeflector() {
        super("ShulkerDeflector", "Automatically reflects shulker's projectiles", ModuleType.COMBAT);
    }

    @Override
    public void tick() {

    }

    boolean inHitRange(Entity attacker, Entity target) {
        return attacker.getCameraPosVec(1f).distanceTo(target.getPos().add(0, target.getHeight() / 2, 0)) <= Objects.requireNonNull(ShadowMain.client.interactionManager).getReachDistance();
    }

    @Override
    public void onFastTick() {
        for (Entity entity : Objects.requireNonNull(ShadowMain.client.world).getEntities()) {
            if (entity instanceof ShulkerBulletEntity sbe && inHitRange(Objects.requireNonNull(ShadowMain.client.player), sbe)) {
                if (checkOwner.getValue() && sbe.getOwner() != null && sbe.getOwner().equals(ShadowMain.client.player)) {
                    continue;
                }
                Objects.requireNonNull(ShadowMain.client.interactionManager).attackEntity(ShadowMain.client.player, sbe);
            }
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
