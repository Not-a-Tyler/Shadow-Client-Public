/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.theme;

import java.awt.Color;

public interface Theme {
    String getName();

    Color getAccent();

    Color getHeader();

    Color getModule();

    Color getConfig();

    Color getActive();

    Color getInactive();
}
