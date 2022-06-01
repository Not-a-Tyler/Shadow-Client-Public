/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.argument;

import net.shadow.client.feature.command.exception.CommandException;

public class IntegerArgumentParser implements ArgumentParser<Integer> {
    @Override
    public Integer parse(String argument) throws CommandException {
        try {
            return Integer.parseInt(argument);
        } catch (Exception e) {
            throw new CommandException("Invalid argument \"" + argument + "\": Expected a number", "Provide a valid number");
        }
    }
}
