/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.IntegerArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class Damage extends Command {
    public Damage() {
        super("Damage", "Applies damage to your player", "damage", "kms");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.NUMBER, "(amount)"));
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("1", "2", "69");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide damage amount");

        if (ShadowMain.client.interactionManager.hasCreativeInventory() || ShadowMain.client.player.isSpectator()) {
            error("You need to be in survival or adventure mode to damage yourself");
            return;
        }

        int amount = new IntegerArgumentParser().parse(args[0]);
        if (amount == 0) {
            error("Listen I know you are scared to do it but heaven awaits you");
            return;
        }
        applyDamage(amount);
        message("Applied Damage");
    }


    private void applyDamage(int amount) {
        Vec3d pos = ShadowMain.client.player.getPos();

        for (int i = 0; i < 80; i++) {
            sendPosition(pos.x, pos.y + amount + 2.1, pos.z, false);
            sendPosition(pos.x, pos.y + 0.05, pos.z, false);
        }

        sendPosition(pos.x, pos.y, pos.z, true);
    }

    private void sendPosition(double x, double y, double z, boolean onGround) {
        ShadowMain.client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
}
