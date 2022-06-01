/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.gui.screen.SpotLightScreen;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.feature.module.NoNotificationDefault;

@NoNotificationDefault
public class Spotlight extends Module {
    public Spotlight() {
        super("Spotlight", "Opens the spotlight menu", ModuleType.RENDER);
    }

    @Override
    public void tick() {
        client.setScreen(new SpotLightScreen());
        setEnabled(false);
    }

    @Override
    public void enable() {

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
