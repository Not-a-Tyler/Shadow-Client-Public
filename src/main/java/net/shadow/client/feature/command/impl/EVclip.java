/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.entity.Entity;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.IntegerArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class EVclip extends Command {
    public EVclip() {
        super("EVclip", "VClip with an entity", "evc", "evclip", "entityVclip");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("1", "2", "69");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.NUMBER, "(amount)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide height");

        if (!ShadowMain.client.player.hasVehicle()) {
            error("You're not riding an entity");
            return;
        }

        int up = new IntegerArgumentParser().parse(args[0]);

        Entity vehicle = ShadowMain.client.player.getVehicle();
        vehicle.updatePosition(vehicle.getX(), vehicle.getY() + up, vehicle.getZ());
        message("Teleported entity");
    }
}
