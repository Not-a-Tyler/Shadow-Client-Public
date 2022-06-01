/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;

public class NoSwing extends Module {

    public NoSwing() {
        super("NoSwing", "Stop hand swinging", ModuleType.COMBAT);
    }

    @EventListener(type=EventType.PACKET_SEND)
    void onSentPacket(PacketEvent event){
        if (event.getPacket() instanceof HandSwingC2SPacket) {
            event.setCancelled(true);
        }
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
}
