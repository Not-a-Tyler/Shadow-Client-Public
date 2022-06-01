/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;

import java.util.ArrayList;
import java.util.List;

public class ReverseKnockback extends Module {

    final List<PlayerMoveC2SPacket> dontRepeat = new ArrayList<>();

    public ReverseKnockback() {
        super("ReverseKnockback", "Reverse the knockback you deal", ModuleType.MISC);
        //        Events.registerEventHandlerClass(this);
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

    @EventListener(type = EventType.PACKET_SEND)
    void packetSend(PacketEvent event) {
        if (!this.isEnabled()) return;
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (dontRepeat.contains(packet)) {
                dontRepeat.remove(packet);
                return;
            }
            event.setCancelled(true);
            double x = packet.getX(0);
            double y = packet.getY(0);
            double z = packet.getZ(0);

            PlayerMoveC2SPacket newPacket;
            if (packet instanceof PlayerMoveC2SPacket.Full) {
                newPacket = new PlayerMoveC2SPacket.Full(x, y, z, MathHelper.wrapDegrees(client.player.getYaw() + 180), 0, packet.isOnGround());
            } else {
                newPacket = new PlayerMoveC2SPacket.LookAndOnGround(MathHelper.wrapDegrees(client.player.getYaw() + 180), 0, packet.isOnGround());
            }
            dontRepeat.add(newPacket);
            client.player.networkHandler.getConnection().send(newPacket);
        }
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
        }
    }
}
