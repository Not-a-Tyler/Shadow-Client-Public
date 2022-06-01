/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;

public class Godmode extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Vanilla).name("Mode").description("The mode to get god in").get());
    int ticks;

    public Godmode() {
        super("Godmode", "God mods", ModuleType.WORLD);
    }

    @EventListener(type = EventType.PACKET_SEND)
    void giveAShit(PacketEvent event) {
        if (mode.getValue().equals(Mode.Portal)) {
            if (event.getPacket() instanceof TeleportConfirmC2SPacket) {
                event.setCancelled(true);
            }
        }
    }

    @EventListener(type = EventType.PACKET_RECEIVE)
    void giveTwoShits(PacketEvent event) {
        if (mode.getValue().equals(Mode.Vanilla)) {
            if (event.getPacket() instanceof DeathMessageS2CPacket) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof EntityTrackerUpdateS2CPacket) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof HealthUpdateS2CPacket) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void tick() {
        if (mode.getValue() == Mode.Matrix) {
            ticks++;
            if (ticks % 10 == 0) {
                for (int i = 0; i < 2; i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), client.player.getY() + 5, client.player.getZ(), true));
                }
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

    public enum Mode {
        Vanilla, Matrix, Portal
    }
}
