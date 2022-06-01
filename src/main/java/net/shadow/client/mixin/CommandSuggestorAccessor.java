/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CommandSuggestor.class)
public interface CommandSuggestorAccessor {
    @Invoker("provideRenderText")
    OrderedText invokeProvideRenderText(String original, int index);
}
