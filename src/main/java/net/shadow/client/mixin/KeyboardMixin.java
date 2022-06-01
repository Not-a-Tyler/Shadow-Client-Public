/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.KeyboardEvent;
import net.shadow.client.helper.manager.KeybindingManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Shadow
    private boolean repeatEvents;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("RETURN"))
    void postKeyPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (window == this.client.getWindow().getHandle() && ShadowMain.client.currentScreen == null && System.currentTimeMillis() - ShadowMain.lastScreenChange > 10) { // make sure we are in game and the screen has been there for at least 10 ms
            if (ShadowMain.client.player == null || ShadowMain.client.world == null) {
                return; // again, make sure we are in game and exist
            }
            KeybindingManager.updateSingle(key, action);
            Events.fireEvent(EventType.KEYBOARD, new KeyboardEvent(key, action));
        }
    }

    @Inject(method = "setRepeatEvents", at = @At("HEAD"), cancellable = true)
    void repeatEvents(boolean repeatEvents, CallbackInfo ci) {
        this.repeatEvents = true;
        ci.cancel();
    }
}
