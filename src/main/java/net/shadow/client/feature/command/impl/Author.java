/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;

public class Author extends Command {
    public Author() {
        super("Author", "Sets the author of a book", "author", "setAuthor");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return new PossibleArgument(ArgumentType.STRING, "(new author)");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide author username");

        if (!ShadowMain.client.interactionManager.hasCreativeInventory()) {
            error("You must be in creative mode to do this!");
            return;
        }

        ItemStack heldItem = ShadowMain.client.player.getInventory().getMainHandStack();

        if (!heldItem.isOf(Items.WRITTEN_BOOK)) {
            error("You must hold a written book");
            return;
        }
        String author = String.join(" ", args);
        heldItem.setSubNbt("author", NbtString.of(author));
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("Notch", "Newton", "Your fucking father");
    }
}
