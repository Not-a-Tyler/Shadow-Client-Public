/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

public class AdBlock extends Module {
    long blocked = 0;

    public AdBlock() {
        super("AdBlock", "Blocks the /ad command on minehut from sending to you", ModuleType.MISC);
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof GameMessageS2CPacket msg) {
                if (msg.getMessage().getString().contains("[AD]") || msg.getMessage().getString().contains("[Minehut]") || msg.getMessage().getString().contains("https://minecraftservers.org/vote/443456")) {
                    event.setCancelled(true);
                    blocked++;
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
        return blocked + "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
