/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class ClientSettings extends Module {
    @Getter
    final StringSetting prefix = this.config.create(new StringSetting.Builder(">").name("Prefix").description("The prefix to use for commands").get());

    public ClientSettings() {
        super("ClientSettings", "Configuration for the client", ModuleType.MISC);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        setEnabled(false);
        Notification.create(5000, "ClientSettings", Notification.Type.INFO, "No need to enable this");
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
