/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.theme.impl;

import net.shadow.client.feature.gui.clickgui.theme.Theme;

import java.awt.Color;

public class Coffee implements Theme {

    static final Color accent = new Color(0x3AD99D);
    static final Color header = new Color(0xFF1D2525, true);
    static final Color module = new Color(0xFF171E1F, true);
    static final Color config = new Color(0xFF111A1A, true);
    static final Color active = new Color(21, 157, 204, 255);
    static final Color inactive = new Color(66, 66, 66, 255);

    @Override
    public String getName() {
        return "Coffee";
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
