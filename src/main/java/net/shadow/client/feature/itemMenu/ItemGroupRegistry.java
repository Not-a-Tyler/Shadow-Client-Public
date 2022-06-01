/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.itemMenu;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ItemGroupRegistry {
    public static final List<ShadItemGroupEntry> groups = Util.make(() -> {
        List<ShadItemGroupEntry> entries = new ArrayList<>();
        entries.add(new ShadItemGroupEntry(new ShadItemGroup("Exploits", new ItemStack(Items.ARMOR_STAND)), "exploit"));
        entries.add(new ShadItemGroupEntry(new ShadItemGroup("Grief", new ItemStack(Items.TNT)), "grief"));
        entries.add(new ShadItemGroupEntry(new ShadItemGroup("Special", new ItemStack(Items.STRUCTURE_VOID)), "special"));
        return entries;
    });

    public static void addItem(String id, ItemStack stack) {
        ShadItemGroupEntry se = groups.stream().filter(shadItemGroupEntry -> shadItemGroupEntry.id.equals(id)).findFirst().orElseThrow();
        se.group.addItem(stack);
    }

    public static void addItem(String id, Item item, String nbt) {
        ShadItemGroupEntry se = groups.stream().filter(shadItemGroupEntry -> shadItemGroupEntry.id.equals(id)).findFirst().orElseThrow();
        se.group.addItem(item, nbt);
    }

    public static void init() {
        initExploits();
    }

    // COURTESY OF SATURNS SHITTY CODE, CONVERTED WITH A CODEGEN
    // DO NOT COMPLAIN ABOUT THIS
    static void initExploits() {
        try {
            InputStream is = ItemGroupRegistry.class.getClassLoader().getResourceAsStream("itemRegistry.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                String gid = split[0];
                String iid = split[1];
                Item i = Registry.ITEM.get(new Identifier(iid));
                String nbt = new String(Base64.getDecoder().decode(split[2]));
                addItem(gid, i, nbt);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    record ShadItemGroupEntry(ShadItemGroup group, String id) {
    }
}
