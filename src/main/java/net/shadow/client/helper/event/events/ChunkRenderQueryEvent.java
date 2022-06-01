/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.shadow.client.helper.event.events.base.NonCancellableEvent;

public class ChunkRenderQueryEvent extends NonCancellableEvent {
    private boolean shouldRender = false;
    private boolean wasModified = false;

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
        this.wasModified = true;
    }

    public boolean wasModified() {
        return wasModified;
    }

    public boolean shouldRender() {
        return shouldRender;
    }
}
