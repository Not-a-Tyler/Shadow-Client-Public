/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.mixin.IEntityVelocityUpdateS2CPacketMixin;

public class Velocity extends Module {

    final DoubleSetting multiplierX = this.config.create(new DoubleSetting.Builder(0.2).name("Horizontal velocity").description("How much to multiply X and Z velocity by").min(-2.5).max(2.5).precision(1).get());
    final DoubleSetting multiplierY = this.config.create(new DoubleSetting.Builder(0.2).name("Vertical velocity").description("How much to multiply Y velocity by").min(-2.5).max(2.5).precision(1).get());
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Modify).name("Mode").description("How to modify velocity").get());

    public Velocity() {
        super("Velocity", "Modifies all incoming velocity updates", ModuleType.COMBAT);
        multiplierX.showIf(() -> mode.getValue() == Mode.Modify);
        multiplierY.showIf(() -> mode.getValue() == Mode.Modify);
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!this.isEnabled() || ShadowMain.client.player == null) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.getId() == ShadowMain.client.player.getId()) {
                if (mode.getValue() == Mode.Modify) {
                    double velX = packet.getVelocityX() / 8000d; // don't ask me why they did this
                    double velY = packet.getVelocityY() / 8000d;
                    double velZ = packet.getVelocityZ() / 8000d;
                    velX *= multiplierX.getValue();
                    velY *= multiplierY.getValue();
                    velZ *= multiplierX.getValue();
                    IEntityVelocityUpdateS2CPacketMixin jesusFuckingChrist = (IEntityVelocityUpdateS2CPacketMixin) packet;
                    jesusFuckingChrist.setVelocityX((int) (velX * 8000));
                    jesusFuckingChrist.setVelocityY((int) (velY * 8000));
                    jesusFuckingChrist.setVelocityZ((int) (velZ * 8000));
                } else {
                    event.setCancelled(true);
                }
            }
        });
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

    }

    @Override
    public void onHudRender() {

    }

    public enum Mode {
        Modify, Ignore
    }
}
