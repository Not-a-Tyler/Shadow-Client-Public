/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.ModuleType;

import java.util.ArrayList;
import java.util.List;

public class Panic extends Command {

    final List<Module> stored = new ArrayList<>();

    public Panic() {
        super("Panic", "Turns off all modules in case you get caught", "panic", "p", "disableall");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "hard", "restore"));
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            stored.clear();
            message("Disabling all non-render modules");
            message("Specify \"hard\" to disable all modules and wipe chat");
            message("Specify \"restore\" to restore all enabled modules before the panic");
            for (Module module : ModuleRegistry.getModules()) {
                if (module.getModuleType() != ModuleType.RENDER && module.isEnabled()) {
                    stored.add(module);
                    module.setEnabled(false);
                }
            }
        } else if (args[0].equalsIgnoreCase("hard")) {
            stored.clear();
            for (Module module : ModuleRegistry.getModules()) {
                if (module.isEnabled()) {
                    stored.add(module);
                    module.setEnabled(false);
                }
            }
            ShadowMain.client.inGameHud.getChatHud().clear(true);
        } else if (args[0].equalsIgnoreCase("restore")) {
            if (stored.size() == 0) {
                error("The stored module list is empty");
            } else {
                for (Module module : stored) {
                    if (!module.isEnabled()) {
                        module.setEnabled(true);
                    }
                }
            }
            stored.clear();
        }
    }
}
