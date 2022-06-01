/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.item.ItemGroup;
import net.shadow.client.mixinUtil.ItemGroupDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemGroup.class)
public class ItemGroupMixin implements ItemGroupDuck {

    @Shadow
    @Final
    @Mutable
    public static ItemGroup[] GROUPS;

    @Override
    public void expandArray() {
        ItemGroup[] tempGroups = GROUPS;
        GROUPS = new ItemGroup[GROUPS.length + 1];
        System.arraycopy(tempGroups, 0, GROUPS, 0, tempGroups.length);
    }
}
