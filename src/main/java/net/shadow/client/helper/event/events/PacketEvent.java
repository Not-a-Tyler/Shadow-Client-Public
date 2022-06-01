/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.minecraft.network.Packet;
import net.shadow.client.helper.event.events.base.Event;

public class PacketEvent extends Event {

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
