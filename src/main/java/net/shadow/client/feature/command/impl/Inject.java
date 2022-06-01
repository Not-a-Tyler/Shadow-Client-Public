/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class Inject extends Command {
    public Inject() {
        super("Inject", "Injects a chunk of nbt into the target item", "inject", "inj", "addNbt");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "(nbt)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide NBT");
        ItemStack is = ShadowMain.client.player.getMainHandStack();

        String nString = String.join(" ", args);
        NbtCompound old = is.getOrCreateNbt();
        try {
            NbtCompound ncNew = StringNbtReader.parse(nString);
            old.copyFrom(ncNew);
            success("Item modified");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
}
