/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.screen;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.gui.FastTickable;
import net.shadow.client.helper.render.Renderer;

import java.awt.Color;

public class TestScreen extends ClientScreen implements FastTickable {

    @Override
    public void onFastTick() {
    }

    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
        Renderer.R2D.renderQuad(stack, Color.WHITE, 0, 0, width, height);
        Renderer.R2D.renderRoundedQuad(stack, new Color(200, 200, 200), 50, 50, 150, 150, 10, 20);
        Renderer.R2D.renderRoundedShadow(stack, new Color(0, 0, 0, 100), 50, 50, 150, 150, 10, 20, -5);


        super.renderInternal(stack, mouseX, mouseY, delta);
    }

}
