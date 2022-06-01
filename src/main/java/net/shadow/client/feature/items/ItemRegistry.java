/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.items;

import net.shadow.client.feature.items.impl.Backdoor;
import net.shadow.client.feature.items.impl.Fireball;
import net.shadow.client.feature.items.impl.Nuke;
import net.shadow.client.feature.items.impl.Plague;
import net.shadow.client.feature.items.impl.Poof;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    public static final ItemRegistry instance = new ItemRegistry();
    final List<Item> items = new ArrayList<>();

    private ItemRegistry() {
        init();
    }

    void init() {
        items.clear();
        items.add(new Nuke());
        items.add(new Plague());
        items.add(new Poof());
        items.add(new Backdoor());
        items.add(new Fireball());
    }

    public List<Item> getItems() {
        return items;
    }
}
