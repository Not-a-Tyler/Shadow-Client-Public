/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class NoLevitation extends Module {

    public NoLevitation() {
        super("NoLevitation", "Prevents the levitation effect from working", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if (FabricLoader.getInstance().isModLoaded("meteor-client")) {
            Notification.create(4000, "NoLevitation", Notification.Type.ERROR, "Meteor is currently loaded and prevents this from working. Use meteor's NoLevitation");
            setEnabled(false);
        }
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

