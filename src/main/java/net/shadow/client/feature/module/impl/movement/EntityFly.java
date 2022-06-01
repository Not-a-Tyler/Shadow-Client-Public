/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class EntityFly extends Module {

    final KeyBinding down = new KeyBinding("", GLFW.GLFW_KEY_LEFT_ALT, "");
    Entity lastRide = null;

    public EntityFly() {
        super("EntityFly", "Allows you to fly with any entity", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
            return;
        }
        Entity vehicle = ShadowMain.client.player.getVehicle();
        if (vehicle == null) {
            return;
        }
        lastRide = vehicle;
        vehicle.setNoGravity(true);
        if (vehicle instanceof MobEntity) {
            ((MobEntity) vehicle).setAiDisabled(true);
        }
        GameOptions go = ShadowMain.client.options;
        float y = Objects.requireNonNull(client.player).getYaw();
        int mx = 0, my = 0, mz = 0;
        if (go.jumpKey.isPressed()) {
            my++;
        }
        if (go.backKey.isPressed()) {
            mz++;
        }
        if (go.leftKey.isPressed()) {
            mx--;
        }
        if (go.rightKey.isPressed()) {
            mx++;
        }
        if (down.isPressed()) {
            my--;
        }
        if (go.forwardKey.isPressed()) {
            mz--;
        }
        double ts = 1;
        double s = Math.sin(Math.toRadians(y));
        double c = Math.cos(Math.toRadians(y));
        double nx = ts * mz * s;
        double nz = ts * mz * -c;
        double ny = ts * my;
        nx += ts * mx * -c;
        nz += ts * mx * -s;
        Vec3d nv3 = new Vec3d(nx, ny - 0.1, nz);
        vehicle.setVelocity(nv3);
        vehicle.setYaw(client.player.getYaw());
        VehicleMoveC2SPacket p = new VehicleMoveC2SPacket(vehicle);
        Objects.requireNonNull(client.getNetworkHandler()).sendPacket(p);
    }

    @Override
    public void enable() {
        Utils.Logging.message("Press left alt to descend");
    }

    @Override
    public void disable() {
        if (lastRide != null) {
            lastRide.setNoGravity(false);
        }
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
