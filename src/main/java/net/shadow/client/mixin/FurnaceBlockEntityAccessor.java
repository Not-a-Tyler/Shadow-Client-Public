/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceBlockEntityAccessor {
    @Accessor("burnTime")
    int getBurnTime();

    @Accessor("fuelTime")
    int getFuelTime();

    @Accessor("cookTime")
    int getCookTime();
}
