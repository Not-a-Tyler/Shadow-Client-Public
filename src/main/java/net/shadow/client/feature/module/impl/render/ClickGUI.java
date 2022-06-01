/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.feature.module.NoNotificationDefault;
import org.lwjgl.glfw.GLFW;

@NoNotificationDefault
public class ClickGUI extends Module {
    int t = 2;

    public ClickGUI() {
        super("ClickGUI", "A visual manager for all modules", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_RIGHT_SHIFT + "");
    }

    @Override
    public void tick() {
        t--;
        if (t == 0) {
            ShadowMain.client.setScreen(net.shadow.client.feature.gui.clickgui.ClickGUI.instance());
            setEnabled(false);
        }
    }

    @Override
    public void enable() {
        t = 2;
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
