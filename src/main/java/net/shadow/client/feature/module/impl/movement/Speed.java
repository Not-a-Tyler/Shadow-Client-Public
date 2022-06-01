/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class Speed extends Module {
    static float fovEffectScal = 0;
    static int ticksonground = 0;
    static int ticksjustsneaking = 0;
    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.OnGround).name("Mode").description("How to apply the speed").get());
    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(20).min(5).max(50).name("Speed").description("How fast to go").get());

    public Speed() {
        super("Speed", "Nyooom", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        fovEffectScal = client.options.fovEffectScale;
        if (client.player == null) return;
        switch (mode.getValue()) {
            case OnGround:
                client.player.setSprinting(true);
                client.options.fovEffectScale = 0F;
                client.player.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(Float.parseFloat(speed.getValue() + "") / 50);
                break;

            case BHop:
                client.player.airStrafingSpeed = Float.parseFloat(speed.getValue() + "") / 100;
                if (client.player.isOnGround() && client.player.forwardSpeed != 0) {
                    client.player.jump();
                } else if (client.player.isOnGround() && client.player.sidewaysSpeed != 0) {
                    client.player.jump();
                }
                break;

            case LowHop:
                if (client.player.input.movementForward != 0 || client.player.input.movementSideways != 0) {
                    client.player.setSprinting(true);

                    if (client.player.isOnGround()) client.player.addVelocity(0, 0.3, 0);

                    if (client.player.isOnGround()) return;

                    float sspeed = Float.parseFloat(speed.getValue() + "") / 50;

                    float yaw = client.player.getYaw();
                    float forward = 1;

                    if (client.player.forwardSpeed < 0) {
                        yaw += 180;
                        forward = -0.5f;
                    } else if (client.player.forwardSpeed > 0) forward = 0.5f;

                    if (client.player.sidewaysSpeed > 0) yaw -= 90 * forward;
                    if (client.player.sidewaysSpeed < 0) yaw += 90 * forward;

                    yaw = (float) Math.toRadians(yaw);

                    client.player.setVelocity(-Math.sin(yaw) * sspeed, client.player.getVelocity().y, Math.cos(yaw) * sspeed);
                }
                break;

            case CSGO:
                client.player.setVelocity(client.player.getVelocity().multiply(1.1));
                break;
        }
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
        client.options.fovEffectScale = fovEffectScal;
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
        OnGround, BHop, LowHop, CSGO
    }
}
