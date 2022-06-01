/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.Mouse;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.MouseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void dispatchMouseEvent(long window, int button, int action, int mods, CallbackInfo ci) {
        if (window == ShadowMain.client.getWindow().getHandle()) {
            if (Events.fireEvent(EventType.MOUSE_EVENT, new MouseEvent(button, action))) {
                ci.cancel();
            }
        }
    }
}
