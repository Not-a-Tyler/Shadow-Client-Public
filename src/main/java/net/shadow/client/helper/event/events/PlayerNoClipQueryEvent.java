/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.minecraft.entity.player.PlayerEntity;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;

public class PlayerNoClipQueryEvent extends NonCancellableEvent {

    final PlayerEntity player;
    NoClipState state = NoClipState.UNSET;

    public PlayerNoClipQueryEvent(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public NoClipState getNoClipState() {
        return state;
    }

    public void setNoClipState(NoClipState state) {
        this.state = state;
    }

    public boolean getNoClip() {
        if (state == NoClipState.UNSET) {
            return player.isSpectator();
        } else {
            return state == NoClipState.ACTIVE;
        }
    }

    public enum NoClipState {
        UNSET, ACTIVE, INACTIVE
    }
}
