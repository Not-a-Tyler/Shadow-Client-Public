/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.shadow.client.feature.gui.screen.AddonManagerScreen;
import net.shadow.client.feature.gui.screen.HudEditorScreen;
import net.shadow.client.feature.gui.widget.RoundButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {
    protected GameMenuMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("RETURN"))
    void addAddons(CallbackInfo ci) {
        addDrawableChild(new RoundButton(RoundButton.STANDARD, 5, 5, 60, 20, "Addons", () -> {
            assert client != null;
            client.setScreen(new AddonManagerScreen());
            //            client.setScreen(new StatsScreen());
        }));
        addDrawableChild(new RoundButton(RoundButton.STANDARD, 5, 30, 60, 20, "Edit HUD", () -> client.setScreen(new HudEditorScreen())));
    }
}
