/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(DebugHud.class)
public interface IDebugHudAccessor {
    @Invoker("getLeftText")
    List<String> callGetLeftText();

    @Invoker("getRightText")
    List<String> callGetRightText();
}
