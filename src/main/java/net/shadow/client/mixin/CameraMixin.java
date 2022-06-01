/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.shadow.client.feature.module.impl.render.NoLiquidFog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {
    @Inject(method = "getSubmersionType", at = @At("RETURN"), cancellable = true)
    void pretendEverythingsFine(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (NoLiquidFog.INSTANCE != null && NoLiquidFog.INSTANCE.isEnabled() && (cir.getReturnValue() == CameraSubmersionType.WATER || cir.getReturnValue() == CameraSubmersionType.LAVA)) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }
}
