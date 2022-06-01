/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.IntegerArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class ItemSpoof extends Command {
    public ItemSpoof() {
        super("ItemSpoof", "Give yourself an item client side", "iSpoof", "itemSpoof", "ghostItem", "clientGive");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, Registry.ITEM.stream().map(p -> Registry.ITEM.getId(p).toString()).toList().toArray(String[]::new)), new PossibleArgument(ArgumentType.NUMBER, "(amount)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 2, "Provide item and amount");

        IntegerArgumentParser integerArgumentParser = new IntegerArgumentParser();
        int amount = integerArgumentParser.parse(args[1]);
        Identifier i = Identifier.tryParse(args[0]);
        if (i == null) throw new CommandException("Invalid name \"" + args[0] + "\"", "Provide valid item identifier");
        Item item = Registry.ITEM.get(i);
        ItemStack stack = new ItemStack(item, amount);
        ShadowMain.client.player.getInventory().armor.set(3, stack);
    }
}
