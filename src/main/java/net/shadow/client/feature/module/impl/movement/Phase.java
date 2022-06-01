/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.event.events.PlayerNoClipQueryEvent;

import java.util.Objects;

public class Phase extends Module {

    public Phase() {
        super("Phase", "Go through walls when flying (works best with creative)", ModuleType.MOVEMENT);
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled() || ShadowMain.client.player == null || !ShadowMain.client.player.getAbilities().flying) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            Box p = ShadowMain.client.player.getBoundingBox(ShadowMain.client.player.getPose()).offset(0, 0.27, 0).expand(0.25);
            if (p.getYLength() < 2) {
                p = p.expand(0, 1, 0);
            }
            p = p.offset(ShadowMain.client.player.getPos());
            if (pe.getPacket() instanceof PlayerMoveC2SPacket && !Objects.requireNonNull(ShadowMain.client.world).isSpaceEmpty(ShadowMain.client.player, p)) {
                event.setCancelled(true);
            }
        });
        Events.registerEventHandler(EventType.NOCLIP_QUERY, event -> {
            if (!getNoClipState(((PlayerNoClipQueryEvent) event).getPlayer())) {
                return;
            }
            ((PlayerNoClipQueryEvent) event).setNoClipState(PlayerNoClipQueryEvent.NoClipState.ACTIVE);
        });
    }

    @Override
    public void tick() {
    }

    public boolean getNoClipState(PlayerEntity pe) {
        return this.isEnabled() && pe.getAbilities().flying;
    }

    @Override
    public void enable() {
        Objects.requireNonNull(ShadowMain.client.player).setPose(EntityPose.STANDING);
        ShadowMain.client.player.setOnGround(false);
        ShadowMain.client.player.fallDistance = 0;
        ShadowMain.client.player.setVelocity(0, 0, 0);
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return getNoClipState(ShadowMain.client.player) ? "Active" : null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (Objects.requireNonNull(ShadowMain.client.player).getAbilities().flying) {
            ShadowMain.client.player.setPose(EntityPose.STANDING);
            ShadowMain.client.player.setOnGround(false);
            ShadowMain.client.player.fallDistance = 0;
        }
    }

    @Override
    public void onHudRender() {

    }
}
