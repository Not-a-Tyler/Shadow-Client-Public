/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.items.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.shadow.client.feature.items.Item;
import net.shadow.client.feature.items.Option;
import net.shadow.client.helper.nbt.NbtGroup;
import net.shadow.client.helper.nbt.NbtList;
import net.shadow.client.helper.nbt.NbtObject;
import net.shadow.client.helper.nbt.NbtProperty;

public class Nuke extends Item {
    final Option<Integer> o = new Option<>("tntFuse", 120, Integer.class);

    public Nuke() {
        super("Nuke", "Nukes the area");
    }

    @Override
    public ItemStack generate() {
        // {BlockEntityTag:{SpawnData:{entity:{id:"minecraft:tnt",HasVisualFire:1b,Fuse:40}},SpawnPotentials:[{weight:1,data:{entity:{id:"minecraft:tnt",HasVisualFire:1b,Fuse:40}}}]}}
        ItemStack spawn = new ItemStack(Items.SPAWNER);
        NbtGroup blt = new NbtGroup(new NbtObject("BlockEntityTag", new NbtProperty("MinSpawnDelay", 1), new NbtProperty("MaxSpawnDelay", 1), new NbtProperty("SpawnRange", 100), new NbtProperty("SpawnCount", 50), new NbtProperty("MaxNearbyEntities", 32766), new NbtObject("SpawnData", new NbtObject("entity", new NbtProperty("id", "minecraft:tnt"), new NbtProperty("HasVisualFire", true), new NbtProperty("Fuse", o.getValue()))), new NbtList("SpawnPotentials", new NbtObject("", new NbtProperty("weight", 1), new NbtObject("data", new NbtObject("entity", new NbtProperty("id", "minecraft:tnt"), new NbtProperty("HasVisualFire", true), new NbtProperty("Fuse", o.getValue()), new NbtProperty("NoGravity", true), new NbtList("Motion", new NbtProperty(0d), new NbtProperty(2d), new NbtProperty(0d))))), new NbtObject("", new NbtProperty("weight", 1), new NbtObject("data", new NbtObject("entity", new NbtProperty("id", "minecraft:tnt"), new NbtProperty("HasVisualFire", true), new NbtProperty("Fuse", o.getValue()), new NbtProperty("NoGravity", true), new NbtList("Motion", new NbtProperty(0d), new NbtProperty(-2d), new NbtProperty(0d))))))));
        spawn.setNbt(blt.toCompound());
        return spawn;
    }
}
