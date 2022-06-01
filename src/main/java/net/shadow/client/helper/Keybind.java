/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper;

import net.minecraft.client.util.InputUtil;
import net.shadow.client.ShadowMain;

public record Keybind(int keycode) {

    public boolean isPressed() {
        if (keycode < 0) {
            return false;
        }
        boolean isActuallyPressed = InputUtil.isKeyPressed(ShadowMain.client.getWindow().getHandle(), keycode);
        return ShadowMain.client.currentScreen == null && isActuallyPressed;
    }
}
