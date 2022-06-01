/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils.Logging;

public class NoSRP extends Module {

    public NoSRP() {
        super("NoSRP", "No server resource packet", ModuleType.MISC);
    }

    @EventListener(type=EventType.PACKET_RECEIVE)
    void onGotPacket(PacketEvent event){
        if (event.getPacket() instanceof ResourcePackSendS2CPacket p) {
            Logging.message("Fake accepted resource pack!");
            Logging.message("URL:" + p.getURL());
            Logging.message("REQUIRED: " + p.isRequired());
            event.setCancelled(true);
            client.player.networkHandler.sendPacket(new ResourcePackStatusC2SPacket(ResourcePackStatusC2SPacket.Status.ACCEPTED));
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
