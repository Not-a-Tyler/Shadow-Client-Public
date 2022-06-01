/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.gui.screen.ProxyManagerScreen;
import net.shadow.client.feature.gui.widget.RoundButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    public MultiplayerScreenMixin() {
        super(Text.of(""));
    }

    @Inject(method = "init", at = @At("RETURN"))
    void init(CallbackInfo ci) {
        double sourceY = 32 / 2d - 20 / 2d;
        RoundButton proxies = new RoundButton(new Color(40, 40, 40), 5, sourceY, 60, 20, "Proxies", () -> ShadowMain.client.setScreen(new ProxyManagerScreen(this)));
        addDrawableChild(proxies);
    }
}
