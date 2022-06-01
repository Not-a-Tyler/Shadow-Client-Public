/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.movement.Hyperspeed;
import net.shadow.client.feature.module.impl.movement.LongJump;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PlayerNoClipQueryEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", opcode = Opcodes.PUTFIELD))
    void tickNoClip(PlayerEntity playerEntity, boolean value) {
        PlayerNoClipQueryEvent q = new PlayerNoClipQueryEvent(playerEntity);
        Events.fireEvent(EventType.NOCLIP_QUERY, q);
        playerEntity.noClip = q.getNoClip();
    }

    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    void a(CallbackInfoReturnable<Float> cir) {
        Hyperspeed hs = ModuleRegistry.getByClass(Hyperspeed.class);
        if (!hs.isEnabled() || !equals(ShadowMain.client.player)) {
            return;
        }
        cir.setReturnValue((float) (cir.getReturnValue() * hs.speed.getValue()));
    }

    @Inject(method = "jump", at = @At("RETURN"))
    void applyLongJump(CallbackInfo ci) {
        if (!this.equals(ShadowMain.client.player)) {
            return;
        }
        if (ModuleRegistry.getByClass(LongJump.class).isEnabled()) {
            ModuleRegistry.getByClass(LongJump.class).applyLongJumpVelocity();
        }
    }
}
