/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.grief;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;

public class AutoRun extends Module {

    final StringSetting commands = this.config.create(new StringSetting.Builder("/say real;/say hacked").name("Commands").description("commands to run when opped, ; separated").get());

    public AutoRun() {
        super("AutoRun", "Automatically runs a series of commands when you get op", ModuleType.GRIEF);
    }

    @Override
    public void tick() {
        if (ShadowMain.client.player.hasPermissionLevel(4)) {
            Utils.Logging.message("You were opped, running commands");
            String[] command = commands.getValue().split(";");
            for (String cmd : command) {
                ShadowMain.client.player.sendChatMessage(cmd);
            }
            this.setEnabled(false);
        }
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
