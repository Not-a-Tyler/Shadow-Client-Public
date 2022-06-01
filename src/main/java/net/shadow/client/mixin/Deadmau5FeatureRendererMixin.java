/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.Deadmau5FeatureRenderer;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.render.MouseEars;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(Deadmau5FeatureRenderer.class)
public class Deadmau5FeatureRendererMixin {
}
