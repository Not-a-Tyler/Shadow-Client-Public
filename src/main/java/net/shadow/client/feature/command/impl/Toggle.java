/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;

public class Toggle extends Command {

    public Toggle() {
        super("Toggle", "Toggles a module", "toggle", "t");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, ModuleRegistry.getModules().stream().map(Module::getName).toList().toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide module name");
        Module m = ModuleRegistry.getByName(String.join(" ", args));
        if (m == null) {
            throw new CommandException("Module not found", "Specify a module name that exists");
        } else {
            m.toggle();
        }
    }
}
