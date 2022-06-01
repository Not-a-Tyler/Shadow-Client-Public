/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.impl.misc.AntiCrash;
import net.shadow.client.mixinUtil.ParticleManagerDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin {
    @ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"), index = 4)
    int re(int value) {
        AntiCrash ac = AntiCrash.instance();
        if (ac.isEnabled() && ac.getCapParticles().getValue()) {
            int partTotal = ((ParticleManagerDuck) ShadowMain.client.particleManager).getTotalParticles();
            int newCount = partTotal + value;
            if (newCount >= ac.getParticleMax().getValue()) {
                int space = (int) Math.floor(ac.getParticleMax().getValue() - partTotal);
                return Math.min(value, Math.max(space, 0));
            }

        }
        return value;
    }
}
