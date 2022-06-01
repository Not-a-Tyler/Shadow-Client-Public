package net.shadow.client.feature.gui.clickgui.theme.impl;

import net.shadow.client.feature.gui.clickgui.theme.Theme;

import java.awt.*;

public class Classic implements Theme {
    static final Color accent = new Color(208,208,208,255);
    static final Color header = new Color(83, 83,83,255);
    static final Color module = new Color(67,67,67,255);
    static final Color config = new Color(83,83,83,255);
    static final Color active = new Color(208,208,208,255);
    static final Color inactive = new Color(99, 99, 99, 255);
    @Override
    public String getName() {
        return "Classic";
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
