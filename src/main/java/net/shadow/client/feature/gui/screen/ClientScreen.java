/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.render.MSAAFramebuffer;

public class ClientScreen extends Screen {
    final int samples;

    public ClientScreen(int samples) {
        super(Text.of(""));
        this.samples = samples;
    }

    public ClientScreen() {
        this(MSAAFramebuffer.MAX_SAMPLES);
    }

    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ShadowMain.client.keyboard.setRepeatEvents(true);
        if (samples != -1) {
            if (!MSAAFramebuffer.framebufferInUse()) {
                MSAAFramebuffer.use(samples, () -> renderInternal(matrices, mouseX, mouseY, delta));
            } else {
                renderInternal(matrices, mouseX, mouseY, delta);
            }
        } else {
            renderInternal(matrices, mouseX, mouseY, delta);
        }
    }
}
