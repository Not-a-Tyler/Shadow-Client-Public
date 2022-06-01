/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;

@RequiredArgsConstructor
public class WorldRenderEvent extends NonCancellableEvent {
    @Getter
    final MatrixStack contextStack;
}
