/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.font;

import net.shadow.client.helper.font.adapter.FontAdapter;
import net.shadow.client.helper.font.adapter.impl.BruhAdapter;
import net.shadow.client.helper.font.renderer.FontRenderer;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FontRenderers {
    private static final List<BruhAdapter> fontRenderers = new ArrayList<>();
    private static FontAdapter normal;
    private static FontAdapter mono;

    public static FontAdapter getRenderer() {
        return normal;
    }

    public static void setRenderer(FontAdapter normal) {
        FontRenderers.normal = normal;
    }

    public static FontAdapter getMono() {
        if (mono == null) {
            int fsize = 18 * 2;
            try {
                mono = (new BruhAdapter(new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("Mono.ttf"))).deriveFont(Font.PLAIN, fsize), fsize)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return mono;
    }

    public static BruhAdapter getCustomSize(int size) {
        int size1 = size;
        size1 *= 2;
        for (BruhAdapter fontRenderer : fontRenderers) {
            if (fontRenderer.getSize() == size1) {
                return fontRenderer;
            }
        }
        int fsize = size1;
        try {
            BruhAdapter bruhAdapter = (new BruhAdapter(new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("Font.ttf"))).deriveFont(Font.PLAIN, fsize), fsize)));
            fontRenderers.add(bruhAdapter);
            return bruhAdapter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
