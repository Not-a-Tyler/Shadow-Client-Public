/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.argument;

import net.shadow.client.feature.command.exception.CommandException;

public interface ArgumentParser<T> {
    T parse(String argument) throws CommandException;
}
