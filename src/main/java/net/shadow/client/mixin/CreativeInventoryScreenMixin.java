/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.gui.screen.NbtEditorScreen;
import net.shadow.client.feature.gui.widget.RoundButton;
import net.shadow.client.helper.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin extends Screen {

    protected CreativeInventoryScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    void postInit(CallbackInfo ci) {
        RoundButton nbtEdit = new RoundButton(RoundButton.STANDARD, 5, 5, 64, 20, "NBT editor", () -> {
            if (ShadowMain.client.player.getInventory().getMainHandStack().isEmpty()) {
                Utils.Logging.error("You need to hold an item!");
            } else {
                ShadowMain.client.setScreen(new NbtEditorScreen(ShadowMain.client.player.getInventory().getMainHandStack()));
            }
        });
        addDrawableChild(nbtEdit);
    }
}
