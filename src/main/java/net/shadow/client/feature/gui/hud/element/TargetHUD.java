/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.hud.element;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.render.TargetHud;

public class TargetHUD extends HudElement {

    public TargetHUD() {
        super("Target HUD", ShadowMain.client.getWindow().getScaledWidth() / 2f + 10, ShadowMain.client.getWindow().getScaledHeight() / 2f + 10, TargetHud.modalWidth, TargetHud.modalHeight);
    }

    @Override
    public void renderIntern(MatrixStack stack) {
        ModuleRegistry.getByClass(TargetHud.class).draw(stack);
    }
}
