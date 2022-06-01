/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.theme.impl;

import net.shadow.client.feature.gui.clickgui.theme.Theme;
import net.shadow.client.feature.module.ModuleRegistry;

import java.awt.Color;

public class Custom implements Theme {
    final net.shadow.client.feature.module.impl.render.Theme theme = ModuleRegistry.getByClass(net.shadow.client.feature.module.impl.render.Theme.class);

    @Override
    public String getName() {
        return "Custom";
    }

    @Override
    public Color getAccent() {
        return theme.accent.getValue();
    }

    @Override
    public Color getHeader() {
        return theme.header.getValue();
    }

    @Override
    public Color getModule() {
        return theme.module.getValue();
    }

    @Override
    public Color getConfig() {
        return theme.configC.getValue();
    }

    @Override
    public Color getActive() {
        return theme.active.getValue();
    }

    @Override
    public Color getInactive() {
        return theme.inactive.getValue();
    }
}
