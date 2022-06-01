/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module;

import net.shadow.client.helper.GameTexture;
import net.shadow.client.helper.Texture;

public enum ModuleType {
    RENDER("Render", GameTexture.ICONS_RENDER.getWhere()), MOVEMENT("Movement", GameTexture.ICONS_MOVE.getWhere()),
    MISC("Miscellaneous", GameTexture.ICONS_MISC.getWhere()), WORLD("World", GameTexture.ICONS_WORLD.getWhere()),
    EXPLOIT("Exploit", GameTexture.ICONS_EXPLOIT.getWhere()), CRASH("Crash", GameTexture.ICONS_CRASH.getWhere()),
    ADDON_PROVIDED("Addons", GameTexture.ICONS_ADDON_PROVIDED.getWhere()),
    GRIEF("Grief", GameTexture.ICONS_GRIEF.getWhere()), COMBAT("Combat", GameTexture.ICONS_COMBAT.getWhere());


    final String name;
    final Texture tex;

    ModuleType(String n, Texture tex) {
        this.name = n;
        this.tex = tex;
    }

    public String getName() {
        return name;
    }

    public Texture getTex() {
        return tex;
    }
}
