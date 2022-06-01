/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.argument;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.exception.CommandException;

@SuppressWarnings("ClassCanBeRecord")
public class PlayerFromNameArgumentParser implements ArgumentParser<PlayerEntity> {
    final boolean ignoreCase;

    public PlayerFromNameArgumentParser(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public PlayerEntity parse(String argument) throws CommandException {
        if (ShadowMain.client.world == null)
            throw new CommandException("World is not loaded", "Join a world or server");
        for (AbstractClientPlayerEntity player : ShadowMain.client.world.getPlayers()) {
            if (ignoreCase) {
                if (player.getGameProfile().getName().equalsIgnoreCase(argument)) return player;
            } else {
                if (player.getGameProfile().getName().equals(argument)) return player;
            }
        }
        throw new CommandException("Invalid argument \"" + argument + "\": Player not found", "Provide the name of an existing player");
    }
}
