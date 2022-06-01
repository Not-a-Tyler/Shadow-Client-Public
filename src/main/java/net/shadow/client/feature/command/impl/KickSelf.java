/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class KickSelf extends Command {
    public KickSelf() {
        super("KickSelf", "Kick yourself from the server", "kickSelf");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "quit", "chars", "packet", "self", "spam", "packets"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide method");

        switch (args[0].toLowerCase()) {
            case "spam":
                for (int i = 0; i < 50; i++) {
                    ShadowMain.client.player.sendChatMessage("/");
                }
                break;

            case "quit":
                ShadowMain.client.world.disconnect();
                break;

            case "chars":
                ShadowMain.client.player.networkHandler.sendPacket(new ChatMessageC2SPacket("\u00a7"));
                break;

            case "packet":
                ShadowMain.client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(3.1e7, 100, 3.1e7, false));
                break;

            case "self":
                PlayerInteractEntityC2SPacket h = PlayerInteractEntityC2SPacket.attack(ShadowMain.client.player, false);
                ShadowMain.client.player.networkHandler.sendPacket(h);
                break;

            case "packets":
                for (int i = 0; i < 5000; i++) {
                    ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND));
                }
                break;
            default:
                error("Mode invalid");
                break;
        }
    }
}
