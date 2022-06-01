/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.PlayerFromNameArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.examples.ExampleServer;
import net.shadow.client.feature.command.exception.CommandException;

import java.util.Objects;

public class Crash extends Command {
    public Crash() {
        super("Crash", "Crash surrounding players with particles", "crash");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new)));
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return ExampleServer.getPlayerNames();
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide player username");
        PlayerFromNameArgumentParser parser = new PlayerFromNameArgumentParser(true);
        PlayerEntity pe = parser.parse(args[0]);
        String player = pe.getGameProfile().getName();
        ShadowMain.client.player.networkHandler.sendPacket(new ChatMessageC2SPacket("/execute as " + player + " at @s run particle flame ~ ~ ~ 1 1 1 0 999999999 normal @s"));
        message("Crashed " + player);
    }
}
