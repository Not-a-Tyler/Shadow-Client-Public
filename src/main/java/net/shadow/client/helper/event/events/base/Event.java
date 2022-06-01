/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events.base;

public class Event {

    boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel(){
        this.cancelled = true;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
