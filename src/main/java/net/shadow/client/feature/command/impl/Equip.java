/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.screen.slot.SlotActionType;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

public class Equip extends Command {
    public Equip() {
        super("Equip", "Equips items", "equip");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("head", "legs", "feet");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "head", "chest", "legs", "feet"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide slot");

        switch (args[0].toLowerCase()) {
            case "head" -> {
                // 39 HEAD - 36 FEET
                ShadowMain.client.interactionManager.clickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 36 + ShadowMain.client.player.getInventory().selectedSlot, 39, SlotActionType.SWAP, ShadowMain.client.player);
                message("Equipped item on head");
            }
            case "chest" -> {
                ShadowMain.client.interactionManager.clickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 36 + ShadowMain.client.player.getInventory().selectedSlot, 39, SlotActionType.SWAP, ShadowMain.client.player);
                message("Equipped item on chest");
            }
            case "legs" -> {
                ShadowMain.client.interactionManager.clickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 36 + ShadowMain.client.player.getInventory().selectedSlot, 39, SlotActionType.SWAP, ShadowMain.client.player);
                message("Equipped item on legs");
            }
            case "feet" -> {
                ShadowMain.client.interactionManager.clickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 36 + ShadowMain.client.player.getInventory().selectedSlot, 39, SlotActionType.SWAP, ShadowMain.client.player);
                message("Equipped item on feet");
            }
            default -> error("Incorrect slot, slots are chest, legs, feet, and head");
        }
    }
}
