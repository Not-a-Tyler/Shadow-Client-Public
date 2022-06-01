/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.world.XRAY;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void overwriteShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(XRAY.class)).isEnabled()) {
            cir.setReturnValue(XRAY.blocks.contains(state.getBlock()));
        }
    }

    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    public void overwriteIsTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (Objects.requireNonNull(ModuleRegistry.getByClass(XRAY.class)).isEnabled()) {
            cir.setReturnValue(!XRAY.blocks.contains(state.getBlock()));
        }
    }
}
