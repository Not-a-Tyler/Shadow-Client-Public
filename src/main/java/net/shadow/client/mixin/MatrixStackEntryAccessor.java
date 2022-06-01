/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MatrixStack.Entry.class)
public interface MatrixStackEntryAccessor {
    @Mutable
    @Accessor("normalMatrix")
    void setNormalMatrix(Matrix3f normal);

    @Mutable
    @Accessor("positionMatrix")
    void setPositionMatrix(Matrix4f position);
}
