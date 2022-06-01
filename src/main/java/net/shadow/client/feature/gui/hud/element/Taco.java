/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.hud.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.Texture;
import net.shadow.client.helper.font.FontRenderers;

public class Taco extends HudElement {
    public Taco() {
        super("Taco", 0, ShadowMain.client.getWindow().getScaledHeight(), 100, 100);
    }

    @Override
    public void renderIntern(MatrixStack stack) {
        if (!net.shadow.client.feature.command.impl.Taco.config.enabled) {
            return;
        }
        net.shadow.client.feature.command.impl.Taco.Frame frame = net.shadow.client.feature.command.impl.Taco.getCurrentFrame();
        if (frame == null) {
            FontRenderers.getRenderer().drawString(stack, "Nothing to taco", 0, 0, 0xFFFFFF);
            return;
        }
        Texture current = frame.getI();

        RenderSystem.disableBlend();
        RenderSystem.setShaderTexture(0, current);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        DrawableHelper.drawTexture(stack, 0, 0, 0, 0, 0, (int) width, (int) height, (int) width, (int) height);
    }
}
