/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.block.AbstractBlock;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.world.XRAY;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void shadow_luminateBlock(CallbackInfoReturnable<Integer> cir) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(XRAY.class)).isEnabled()) {
            cir.setReturnValue(15);
        }
    }
}
