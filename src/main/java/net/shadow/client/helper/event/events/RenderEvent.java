/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.helper.event.events.base.Event;

public class RenderEvent extends Event {

    final MatrixStack stack;

    public RenderEvent(MatrixStack stack) {
        this.stack = stack;
    }

    public MatrixStack getStack() {
        return stack;
    }
}
