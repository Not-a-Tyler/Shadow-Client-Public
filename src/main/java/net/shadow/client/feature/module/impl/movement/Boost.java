/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.feature.module.NoNotificationDefault;

@NoNotificationDefault
public class Boost extends Module {

    final DoubleSetting strength = this.config.create(new DoubleSetting.Builder(3).name("Strength").description("How much to boost you with").min(0.1).max(10).precision(1).get());
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Add).name("Mode").description("How to boost you").get());

    public Boost() {
        super("Boost", "Boosts you into the air", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
            return;
        }
        setEnabled(false);
        Vec3d newVelocity = ShadowMain.client.player.getRotationVector().multiply(strength.getValue());
        if (this.mode.getValue() == Mode.Add) {
            ShadowMain.client.player.addVelocity(newVelocity.x, newVelocity.y, newVelocity.z);
        } else {
            ShadowMain.client.player.setVelocity(newVelocity);
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

    public enum Mode {
        Add, Overwrite
    }
}
