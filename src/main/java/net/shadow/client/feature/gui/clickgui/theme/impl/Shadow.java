/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.theme.impl;

import net.shadow.client.feature.gui.clickgui.theme.Theme;

import java.awt.Color;

public class Shadow implements Theme {
    static final Color accent = new Color(214, 255, 255);
    static final Color header = new Color(23, 23, 23);
    static final Color module = new Color(20, 20, 20);
    static final Color config = new Color(23, 23, 23);
    static final Color active = new Color(101, 101, 101);
    static final Color inactive = new Color(53, 53, 53);

    @Override
    public String getName() {
        return "Shadow";
    }

    @Override
    public Color getAccent() {
        return accent;
    }

    @Override
    public Color getHeader() {
        return header;
    }

    @Override
    public Color getModule() {
        return module;
    }

    @Override
    public Color getConfig() {
        return config;
    }

    @Override
    public Color getActive() {
        return active;
    }

    @Override
    public Color getInactive() {
        return inactive;
    }
}
