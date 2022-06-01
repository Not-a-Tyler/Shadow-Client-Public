/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.IntegerArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class PermissionLevel extends Command {
    public PermissionLevel() {
        super("PermissionLevel", "Sets the player's permission level client side", "permissionlevel", "permlevel", "cperm");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.NUMBER, "1", "2", "3", "4"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide permission level");
        IntegerArgumentParser iap = new IntegerArgumentParser();
        int newPermLevel = iap.parse(args[0]);
        ShadowMain.client.player.setClientPermissionLevel(newPermLevel);
        message("Set the Permission level to [" + newPermLevel + "]");
    }
}
