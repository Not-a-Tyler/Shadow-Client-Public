/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class AdSpammer extends Module {

    final StringSetting message = this.config.create(new StringSetting.Builder("/ad randomserver its so good").name("Message").get());

    public AdSpammer() {
        super("AdSpammer", "Spams minehut ads", ModuleType.MISC);
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
}
