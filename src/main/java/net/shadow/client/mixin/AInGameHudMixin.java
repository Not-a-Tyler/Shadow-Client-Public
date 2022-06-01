/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.gui.notifications.NotificationRenderer;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.helper.AccurateFrameRateCounter;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;
import net.shadow.client.helper.render.MSAAFramebuffer;
import net.shadow.client.helper.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class AInGameHudMixin extends DrawableHelper {
    @Inject(method = "render", at = @At("RETURN"))
    public void postRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        AccurateFrameRateCounter.globalInstance.recordFrame();
        MSAAFramebuffer.use(MSAAFramebuffer.MAX_SAMPLES, () -> {
            for (Module module : ModuleRegistry.getModules()) {
                if (module.isEnabled()) {
                    module.onHudRender();
                }
            }
            Utils.TickManager.render();
            NotificationRenderer.render();

            Events.fireEvent(EventType.HUD_RENDER, new NonCancellableEvent());
        });
        for (Module module : ModuleRegistry.getModules()) {
            if (module.isEnabled()) {
                module.onHudRenderNoMSAA();
            }
        }
    }
}
