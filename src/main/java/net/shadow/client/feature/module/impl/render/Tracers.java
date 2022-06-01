/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;

import java.awt.Color;
import java.util.Comparator;
import java.util.stream.StreamSupport;

public class Tracers extends Module {

    final BooleanSetting entities = this.config.create(new BooleanSetting.Builder(false).name("Show entities").description("Render entities").get());
    final BooleanSetting players = this.config.create(new BooleanSetting.Builder(true).name("Show players").description("Render players").get());

    public Tracers() {
        super("Tracers", "Shows where entities are in relation to you", ModuleType.RENDER);
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
        if (ShadowMain.client.world == null || ShadowMain.client.player == null) {
            return null;
        }
        return StreamSupport.stream(ShadowMain.client.world.getEntities().spliterator(), false).filter(entity -> entity.squaredDistanceTo(ShadowMain.client.player) < 4096 && entity.getUuid() != ShadowMain.client.player.getUuid() && isEntityApplicable(entity)).count() + "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (ShadowMain.client.world == null || ShadowMain.client.player == null) {
            return;
        }
        for (Entity entity : StreamSupport.stream(ShadowMain.client.world.getEntities().spliterator(), false).sorted(Comparator.comparingDouble(value -> -value.distanceTo(ShadowMain.client.player))).toList()) {
            if (entity.squaredDistanceTo(ShadowMain.client.player) > 4096) {
                continue;
            }
            double dc = entity.squaredDistanceTo(ShadowMain.client.player) / 4096;
            dc = Math.abs(1 - dc);
            if (entity.getUuid().equals(ShadowMain.client.player.getUuid())) {
                continue;
            }
            Color c;
            if (entity instanceof PlayerEntity) {
                c = Color.RED;
            } else if (entity instanceof ItemEntity) {
                c = Color.CYAN;
            } else if (entity instanceof EndermanEntity enderman) {
                if (enderman.isProvoked()) {
                    c = Color.YELLOW;
                } else {
                    c = Color.GREEN;
                }
            } else if (entity instanceof HostileEntity) {
                c = Color.YELLOW;
            } else {
                c = Color.GREEN;
            }
            c = Renderer.Util.modify(c, -1, -1, -1, (int) Math.floor(dc * 255));
            if (isEntityApplicable(entity)) {
                Vec3d pos = Utils.getInterpolatedEntityPosition(entity);
                Renderer.R3D.renderLine(Renderer.R3D.getCrosshairVector(), pos.add(0, entity.getHeight() / 2, 0), c, matrices);
            }
        }
    }

    boolean isEntityApplicable(Entity v) {
        return (v instanceof PlayerEntity && players.getValue()) || entities.getValue();
    }

    @Override
    public void onHudRender() {

    }
}
