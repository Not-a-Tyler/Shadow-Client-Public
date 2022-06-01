/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.DoubleArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class HClip extends Command {
    public HClip() {
        super("HClip", "Teleport horizontally", "hclip");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.NUMBER, "(amount)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide distance");

        double brik = new DoubleArgumentParser().parse(args[0]);
        Vec3d forward = Vec3d.fromPolar(0, ShadowMain.client.player.getYaw()).normalize();

        if (ShadowMain.client.player.getAbilities().creativeMode) {
            ShadowMain.client.player.updatePosition(ShadowMain.client.player.getX() + forward.x * brik, ShadowMain.client.player.getY(), ShadowMain.client.player.getZ() + forward.z * brik);
        } else {
            clip(brik);
        }
    }

    private void clip(double blocks) {
        Vec3d pos = ShadowMain.client.player.getPos();
        Vec3d forward = Vec3d.fromPolar(0, ShadowMain.client.player.getYaw()).normalize();
        float oldy = ShadowMain.client.player.getYaw();
        float oldp = ShadowMain.client.player.getPitch();
        sendPosition(pos.x, pos.y + 9, pos.z);
        sendPosition(pos.x, pos.y + 18, pos.z);
        sendPosition(pos.x, pos.y + 27, pos.z);
        sendPosition(pos.x, pos.y + 36, pos.z);
        sendPosition(pos.x, pos.y + 45, pos.z);
        sendPosition(pos.x, pos.y + 54, pos.z);
        sendPosition(pos.x, pos.y + 63, pos.z);
        sendPosition(pos.x + forward.x * blocks, ShadowMain.client.player.getY(), pos.z + forward.z * blocks);
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY() - 8.9, ShadowMain.client.player.getZ());
        sendPosition(ShadowMain.client.player.getX(), ShadowMain.client.player.getY(), ShadowMain.client.player.getZ());
        ShadowMain.client.player.setYaw(oldy);
        ShadowMain.client.player.setPitch(oldp);
    }


    private void sendPosition(double x, double y, double z) {
        ShadowMain.client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true));
    }
}
