/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.notifications.hudNotif;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.render.MSAAFramebuffer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HudNotificationRenderer {
    public static final HudNotificationRenderer instance = new HudNotificationRenderer();
    final List<HudNotification> notifs = new CopyOnWriteArrayList<>();

    void addNotification(HudNotification notif) {
        this.notifs.add(notif);
    }

    public void render(MatrixStack stack) {
        notifs.removeIf(HudNotification::isDead);
        double x = ShadowMain.client.getWindow().getScaledWidth() - 5;
        final double[] y = { 5 };
        MSAAFramebuffer.use(MSAAFramebuffer.MAX_SAMPLES, () -> {
            for (HudNotification notif : notifs) {
                notif.render(stack, x, y[0]);

                double moveAnim = MathHelper.clamp(notif.easeInOutBack(MathHelper.clamp(notif.getAnimProg(), 0, 0.5) * 2), 0, 1);
                y[0] += (notif.getHeight() + 5) * moveAnim;
            }
        });
    }
}
