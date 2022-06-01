/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.Rotations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
    public float overwriteYaw(float oldValue, LivingEntity le) {
        if (Rotations.isEnabled() && le.equals(ShadowMain.client.player)) {
            return Rotations.getClientYaw();
        }
        return oldValue;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 3, at = @At(value = "STORE", ordinal = 0))
    public float overwriteHeadYaw(float oldValue, LivingEntity le) {
        if (le.equals(ShadowMain.client.player) && Rotations.isEnabled()) {
            return Rotations.getClientYaw();
        }
        return oldValue;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 5, at = @At(value = "STORE", ordinal = 3))
    public float overwritePitch(float oldValue, LivingEntity le) {
        if (le.equals(ShadowMain.client.player) && Rotations.isEnabled()) {
            return Rotations.getClientPitch();
        }
        return oldValue;
    }

}
