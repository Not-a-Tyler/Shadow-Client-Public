/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.hud.element;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.font.FontRenderers;

public class TabGui extends HudElement {
    net.shadow.client.feature.module.impl.render.TabGui tgui;

    public TabGui() {
        super("Tab gui", 5, 100, 0, ModuleType.values().length * FontRenderers.getRenderer().getMarginHeight() + 4);
        double longest = 0;
        for (ModuleType value : ModuleType.values()) {
            longest = Math.max(FontRenderers.getRenderer().getStringWidth(value.getName()), longest);
        }
        longest = Math.ceil(longest + 1);
        width = 2 + 1.5 + 2 + longest + 3;
    }

    net.shadow.client.feature.module.impl.render.TabGui getTgui() {
        if (tgui == null) tgui = ModuleRegistry.getByClass(net.shadow.client.feature.module.impl.render.TabGui.class);
        return tgui;
    }

    @Override
    public void renderIntern(MatrixStack stack) {
        stack.push();
        getTgui().render(stack);
        stack.pop();
    }
}
