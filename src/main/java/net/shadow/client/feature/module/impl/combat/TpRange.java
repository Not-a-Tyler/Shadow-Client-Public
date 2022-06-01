/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.MouseEvent;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;

import java.awt.Color;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TpRange extends Module {
    static final ExecutorService esv = Executors.newFixedThreadPool(1);
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.PaperBypass).name("Mode").description("How to exploit the range, Instant works on vanilla, PaperBypass on almost everything").get());
    final AtomicBoolean running = new AtomicBoolean(false);
    Vec3d spoofedPos = null;
    Vec3d previousSpoofedPos = null;

    public TpRange() {
        super("TpRange", "Hits someone from VERY far away", ModuleType.COMBAT);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled()) {
                return;
            }
            if (ShadowMain.client.player == null || ShadowMain.client.world == null) {
                return;
            }
            if (ShadowMain.client.currentScreen != null) {
                return;
            }
            MouseEvent me = (MouseEvent) event;
            if (me.getAction() == 1 && me.getButton() == 0) {
                if (running.get()) {
                    Notification.create(5000, "TpRange", Notification.Type.WARNING, "Already exploiting, please wait a bit");
                } else {
                    esv.execute(this::theFunny);
                }
            }
        });
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof PlayerPositionLookS2CPacket && running.get()) {
                event.setCancelled(true);
            }
        });
    }

    void doIt() {
        Vec3d goal = Objects.requireNonNull(ShadowMain.client.player).getRotationVec(1f).multiply(200);
        Box b = ShadowMain.client.player.getBoundingBox().stretch(goal).expand(1, 1, 1);
        EntityHitResult ehr = ProjectileUtil.raycast(ShadowMain.client.player, ShadowMain.client.player.getCameraPosVec(0), ShadowMain.client.player.getCameraPosVec(0).add(goal), b, Entity::isAttackable, 200 * 200);
        if (ehr == null) {
            return;
        }
        Vec3d pos = ehr.getPos();
        Vec3d orig = ShadowMain.client.player.getPos();

        if (mode.getValue() == Mode.PaperBypass) {
            teleportTo(orig, pos);
            Objects.requireNonNull(ShadowMain.client.interactionManager).attackEntity(ShadowMain.client.player, ehr.getEntity());
            Utils.sleep(100);
            teleportTo(pos, orig);
            ShadowMain.client.player.updatePosition(orig.x, orig.y, orig.z);
        } else {
            PlayerMoveC2SPacket tpToEntity = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false);
            PlayerMoveC2SPacket tpBack = new PlayerMoveC2SPacket.PositionAndOnGround(orig.x, orig.y, orig.z, true);
            Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(tpToEntity);
            Objects.requireNonNull(ShadowMain.client.interactionManager).attackEntity(ShadowMain.client.player, ehr.getEntity());
            ShadowMain.client.getNetworkHandler().sendPacket(tpBack);
        }
    }

    void theFunny() {
        running.set(true);
        doIt();
        running.set(false);
    }

    void teleportTo(Vec3d from, Vec3d pos) {
        double distance = from.distanceTo(pos);
        for (int i = 0; i < distance; i += 2) {
            double prog = i / distance;
            double newX = MathHelper.lerp(prog, from.x, pos.x);
            double newY = MathHelper.lerp(prog, from.y, pos.y);
            double newZ = MathHelper.lerp(prog, from.z, pos.z);
            PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, true);
            Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(p);
            previousSpoofedPos = spoofedPos;
            spoofedPos = new Vec3d(newX, newY, newZ);
            Utils.sleep(10);
        }
        PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true);
        Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(p);
        previousSpoofedPos = null;
        spoofedPos = null;
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
        if (isDebuggerEnabled() && spoofedPos != null && previousSpoofedPos != null) {
            Renderer.R3D.renderLine(spoofedPos, previousSpoofedPos, Color.RED, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }

    public enum Mode {
        PaperBypass, Instant
    }
}
