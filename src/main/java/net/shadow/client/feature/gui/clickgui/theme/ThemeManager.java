/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.theme;

import net.shadow.client.feature.gui.clickgui.theme.impl.Classic;
import net.shadow.client.feature.gui.clickgui.theme.impl.Coffee;
import net.shadow.client.feature.gui.clickgui.theme.impl.Custom;
import net.shadow.client.feature.gui.clickgui.theme.impl.Shadow;
import net.shadow.client.feature.module.ModuleRegistry;

public class ThemeManager {
    static final net.shadow.client.feature.module.impl.render.Theme t = ModuleRegistry.getByClass(net.shadow.client.feature.module.impl.render.Theme.class);
    static final Theme custom = new Custom();
    static final Theme shadow = new Shadow();
    static final Theme bestThemeEver = new Coffee();
    static final Theme classic = new Classic();
    public static Theme getMainTheme() {
        return switch (t.modeSetting.getValue()) {
            case Coffee -> bestThemeEver;
            case Custom -> custom;
            case Shadow -> shadow;
            case Classic -> classic;
        };
    }
}
