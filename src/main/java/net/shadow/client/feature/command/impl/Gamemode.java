/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.world.GameMode;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

import java.util.Arrays;

public class Gamemode extends Command {

    public Gamemode() {
        super("Gamemode", "Switch gamemodes client side", "gamemode", "gm", "gmode");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("survival", "creative", "adventure");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, Arrays.stream(GameMode.values()).map(GameMode::getName).toList().toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (ShadowMain.client.interactionManager == null) {
            return;
        }
        validateArgumentsLength(args, 1, "Provide gamemode");
        GameMode gm = GameMode.byName(args[0], null);
        if (gm == null) throw new CommandException("Invalid gamemode", "Specify a valid gamemode");
        ShadowMain.client.interactionManager.setGameMode(gm);
    }
}
