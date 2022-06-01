/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.misc.MoreChatHistory;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Debug(export = true)
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow
    public abstract int getWidth();

    @ModifyConstant(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", constant = @Constant(intValue = 100))
    int a(int constant) {
        MoreChatHistory hist = ModuleRegistry.getByClass(MoreChatHistory.class);
        if (hist.isEnabled()) return hist.getHistSize();
        else return 100;
    }
}
