/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.clickgui.element.impl.config;

import net.shadow.client.feature.config.SettingBase;
import net.shadow.client.feature.gui.clickgui.element.Element;

public abstract class ConfigBase<C extends SettingBase<?>> extends Element {
    final C configValue;

    public ConfigBase(double x, double y, double width, double height, C configValue) {
        super(x, y, width, height);
        this.configValue = configValue;
    }

    public C getConfigValue() {
        return configValue;
    }
}
