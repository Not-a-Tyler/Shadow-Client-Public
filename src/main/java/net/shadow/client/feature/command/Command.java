/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command;

import net.minecraft.client.MinecraftClient;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.util.Utils;

public abstract class Command extends Utils.Logging {

    public final MinecraftClient client = MinecraftClient.getInstance();
    private final String name;
    private final String description;
    private final String[] aliases;

    public Command(String n, String d, String... a) {
        String first = String.valueOf(d.charAt(0));
        this.name = n;
        this.description = d;
        this.aliases = a;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void onExecute(String[] args) throws CommandException;

    public ExamplesEntry getExampleArguments() {
        return null;
    }

    protected void validateArgumentsLength(String[] args, int requiredLength, String message) throws CommandException {
        if (args.length < requiredLength)
            throw new CommandException("Invalid number of arguments: " + requiredLength + " arguments required", message);
    }

    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return new PossibleArgument(null);
    }

    public record ExamplesEntry(String... examples) {
    }
}
