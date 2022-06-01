/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.gui.clickgui.theme.ThemeManager;
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

public class Swing extends Module {
    static final Color line = new Color(50, 50, 50, 255);
    static BlockPos swinging;

    public Swing() {
        super("Swing", "Swing around like spiderman", ModuleType.MOVEMENT);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled() || ShadowMain.client.currentScreen != null) {
                return;
            }
            MouseEvent me = (MouseEvent) event;
            if (me.getButton() == 0 && me.getAction() == 1) {
                try {
                    HitResult hit = Objects.requireNonNull(ShadowMain.client.player).raycast(200, ShadowMain.client.getTickDelta(), true);
                    swinging = new BlockPos(hit.getPos());
                } catch (Exception ignored) {
                }
            }
        });
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof ClientCommandC2SPacket e && e.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY) {
                event.setCancelled(true);
            }
        });
    }

    @Override
    public void tick() {
        if (swinging == null) {
            return;
        }
        Vec3d diff = Vec3d.of(swinging).add(0.5, 0.5, 0.5).subtract(Utils.getInterpolatedEntityPosition(ShadowMain.client.player)).normalize().multiply(0.4).add(0, 0.03999999910593033 * 2, 0);

        ShadowMain.client.player.addVelocity(diff.x, diff.y, diff.z);
        if (ShadowMain.client.options.sneakKey.isPressed()) {
            swinging = null;
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
        if (swinging == null || ShadowMain.client.player == null) {
            return;
        }
        RenderSystem.defaultBlendFunc();
        Vec3d cringe = new Vec3d(swinging.getX(), swinging.getY(), swinging.getZ());
        Vec3d cringe2 = new Vec3d(swinging.getX() + 0.5, swinging.getY() + 0.5, swinging.getZ() + 0.5);
        Vec3d eSource = Utils.getInterpolatedEntityPosition(ShadowMain.client.player);
        //        Renderer.R3D.renderFilled(cringe, new Vec3d(1, 1, 1), new Color(150, 150, 150, 150), matrices)
        Renderer.R3D.renderFilled(cringe.add(.5, .5, .5).subtract(.25, .25, .25), new Vec3d(.5, .5, .5), ThemeManager.getMainTheme().getInactive(), matrices);
        Renderer.R3D.renderLine(eSource, cringe2, line, matrices);
    }

    @Override
    public void onHudRender() {

    }
}
