/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils;

import java.util.Random;

public class FlightCrash extends Module {

    final DoubleSetting velocity = this.config.create(new DoubleSetting.Builder(7).min(1).max(10).name("Velocity").description("How fast to move").get());
    final DoubleSetting timer = this.config.create(new DoubleSetting.Builder(25).min(20).max(200).name("Timer").description("Timer boost speed").get());
    final DoubleSetting lockat = this.config.create(new DoubleSetting.Builder(200).min(20).max(500).name("LockAT").description("What height to start boosting at").get());
    boolean capture = true;

    public FlightCrash() {
        super("FlightCrash", "Generates a ton of chunks", ModuleType.CRASH);
        //        Events.registerEventHandlerClass(this);
    }

    @Override
    public void tick() {
        Utils.setClientTps(timer.getValue().floatValue());
        ClientPlayerEntity player = client.player;
        client.player.setSprinting(true);
        Vec3d forward = Vec3d.fromPolar(0, player.getYaw()).normalize();
        if (client.player.getY() < lockat.getValue()) {
            player.setVelocity(0, 0.3, 0);
        } else {
            client.options.forwardKey.setPressed(true);
            player.setVelocity(forward.x * velocity.getValue(), 0, forward.z * velocity.getValue());
        }
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
        Utils.setClientTps(20F);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @EventListener(type = EventType.PACKET_SEND)
    void onSentPacket(PacketEvent event) {
        if (!this.isEnabled()) return;
        if (!capture) return;

        if (!(event.getPacket() instanceof PlayerMoveC2SPacket packet)) return;

        if (!(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full))
            return;

        if (client.player.input == null) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        double x = packet.getX(0);
        double y = packet.getY(0);
        double z = packet.getZ(0);

        Packet<?> newPacket;
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
            newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y + randomboolnum(), z, true);
        else
            newPacket = new PlayerMoveC2SPacket.Full(x, y + randomboolnum(), z, packet.getYaw(0), packet.getPitch(0), true);

        capture = false;
        client.player.networkHandler.getConnection().send(newPacket);
        capture = true;
    }

    @Override
    public void onHudRender() {

    }

    private double randomboolnum() {
        if (new Random().nextBoolean()) {
            return Math.random() * 1.5;
        } else {
            return Math.random() * -1.5;
        }
    }
}
