/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.feature.gui.screen.BindScreen;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.helper.util.Utils;

public class Bind extends Command {
    public Bind() {
        super("Bind", "Sets the keybind of a module", "bind");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        if (index == 0)
            return new PossibleArgument(ArgumentType.STRING, ModuleRegistry.getModules().stream().map(Module::getName).toList().toArray(String[]::new));
        return super.getSuggestionsWithType(index, args);
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("Flight", "NoFall", "ClickGui");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide module name");
        String mn = args[0];
        Module module = ModuleRegistry.getByName(mn);
        if (module == null) {
            error("Module not found");
            return;
        }
        BindScreen bs = new BindScreen(module);
        Utils.TickManager.runInNTicks(5, () -> ShadowMain.client.setScreen(bs));
    }
}
