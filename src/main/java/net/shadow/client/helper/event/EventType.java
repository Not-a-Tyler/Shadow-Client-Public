/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event;

import lombok.RequiredArgsConstructor;
import net.shadow.client.helper.event.events.BlockEntityRenderEvent;
import net.shadow.client.helper.event.events.BlockRenderEvent;
import net.shadow.client.helper.event.events.ChunkRenderQueryEvent;
import net.shadow.client.helper.event.events.EntityRenderEvent;
import net.shadow.client.helper.event.events.KeyboardEvent;
import net.shadow.client.helper.event.events.LoreQueryEvent;
import net.shadow.client.helper.event.events.MouseEvent;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.event.events.PlayerNoClipQueryEvent;
import net.shadow.client.helper.event.events.WorldRenderEvent;
import net.shadow.client.helper.event.events.base.Event;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;

@RequiredArgsConstructor
public enum EventType {
    PACKET_SEND(PacketEvent.class), PACKET_RECEIVE(PacketEvent.class), ENTITY_RENDER(EntityRenderEvent.class),
    BLOCK_ENTITY_RENDER(BlockEntityRenderEvent.class), BLOCK_RENDER(BlockRenderEvent.class),
    MOUSE_EVENT(MouseEvent.class), LORE_QUERY(LoreQueryEvent.class), CONFIG_SAVE(NonCancellableEvent.class),
    NOCLIP_QUERY(PlayerNoClipQueryEvent.class), KEYBOARD(KeyboardEvent.class), POST_INIT(NonCancellableEvent.class),
    HUD_RENDER(NonCancellableEvent.class), GAME_EXIT(NonCancellableEvent.class),
    SHOULD_RENDER_CHUNK(ChunkRenderQueryEvent.class), WORLD_RENDER(WorldRenderEvent.class);
    private final Class<? extends Event> expectedType;

    public Class<? extends Event> getExpectedType() {
        return expectedType;
    }
}
