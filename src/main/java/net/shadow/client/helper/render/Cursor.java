/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.render;

import net.shadow.client.ShadowMain;
import org.lwjgl.glfw.GLFW;

public class Cursor {
    public static final long CLICK = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    public static final long STANDARD = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    public static final long TEXT_EDIT = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    public static long HSLIDER = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
    private static long currentCursor = -1;

    public static void setGlfwCursor(long cursor) {
        if (currentCursor == cursor) return;
        currentCursor = cursor;
        GLFW.glfwSetCursor(ShadowMain.client.getWindow().getHandle(), cursor);
    }
}
