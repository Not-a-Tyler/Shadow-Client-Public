/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.config;

import java.util.function.Consumer;

/**
 * A setting describing a boolean
 */
public class BooleanSetting extends SettingBase<Boolean> {

    public BooleanSetting(Boolean defaultValue, String name, String description, Consumer<Boolean> onChanged) {
        super(defaultValue, name, description, onChanged);
    }

    @Override
    public Boolean parse(String value) {
        return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"));
    }

    public static class Builder extends SettingBase.Builder<Builder, Boolean, BooleanSetting> {

        public Builder(Boolean defaultValue) {
            super(defaultValue);
        }

        @Override
        public BooleanSetting get() {
            return new BooleanSetting(defaultValue, name, description, changed);
        }
    }
}
