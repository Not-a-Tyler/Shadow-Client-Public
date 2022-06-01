/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.coloring;

public class PossibleArgument {
    final String[] suggestions;
    final ArgumentType type;

    public PossibleArgument(ArgumentType type, String... suggestions) {
        this.suggestions = suggestions;
        this.type = type;
    }

    public ArgumentType getType() {
        return type;
    }

    public String[] getSuggestions() {
        return suggestions;
    }
}
