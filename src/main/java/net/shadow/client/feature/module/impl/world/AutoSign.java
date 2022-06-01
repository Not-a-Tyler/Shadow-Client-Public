/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class AutoSign extends Module {

    final StringSetting ss1 = this.config.create(new StringSetting.Builder("discord.gg/moles").name("Line 1").description("The text for line 1").get());
    final StringSetting ss2 = this.config.create(new StringSetting.Builder("discord.gg/moles").name("Line 2").description("The text for line 2").get());
    final StringSetting ss3 = this.config.create(new StringSetting.Builder("discord.gg/moles").name("Line 3").description("The text for line 3").get());
    final StringSetting ss4 = this.config.create(new StringSetting.Builder("discord.gg/moles").name("Line 4").description("The text for line 4").get());

    public AutoSign() {
        super("AutoSign", "Automatically write signs when looking at them", ModuleType.WORLD);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    public String[] getText() {
        return new String[] { ss1.getValue(), ss2.getValue(), ss3.getValue(), ss4.getValue() };
    }
}
