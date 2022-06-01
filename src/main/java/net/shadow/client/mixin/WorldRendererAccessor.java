/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.SortedSet;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Accessor("blockBreakingProgressions")
    Long2ObjectMap<SortedSet<BlockBreakingInfo>> getBlockBreakingProgressions();
}
