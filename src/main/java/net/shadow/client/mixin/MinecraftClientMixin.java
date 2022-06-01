/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Session;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.world.FastUse;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;
import net.shadow.client.helper.manager.ConfigManager;
import net.shadow.client.mixinUtil.MinecraftClientDuck;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientDuck {
    @Shadow
    private int itemUseCooldown;

    @Mutable
    @Shadow
    @Final
    private Session session;

    @Inject(method = "stop", at = @At("HEAD"))
    void real(CallbackInfo ci) {
        ConfigManager.saveState();
        Events.fireEvent(EventType.GAME_EXIT, new NonCancellableEvent());
    }

    @Override
    public void setSession(Session newSession) {
        this.session = newSession;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void postInit(RunArgs args, CallbackInfo ci) {
        ShadowMain.INSTANCE.postWindowInit();
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    void preSetScreen(Screen screen, CallbackInfo ci) {
        ShadowMain.lastScreenChange = System.currentTimeMillis();
    }

    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I"))
    public int replaceItemUseCooldown(MinecraftClient minecraftClient) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(FastUse.class)).isEnabled()) {
            return 0;
        } else {
            return this.itemUseCooldown;
        }
    }
}
