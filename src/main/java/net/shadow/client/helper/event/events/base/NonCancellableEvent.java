/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events.base;

public class NonCancellableEvent extends Event {

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        throw new IllegalStateException("Event cannot be cancelled.");
    }
}
