/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.PlayerFromNameArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

import java.util.Objects;

public class ItemData extends Command {
    public ItemData() {
        super("ItemData", "Show item data from other player's inventories", "itemData", "idata");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.PLAYER, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new)), new PossibleArgument(ArgumentType.STRING, "hand", "offhand", "head", "chest", "legs", "feet"), new PossibleArgument(ArgumentType.STRING, "--onlyShow"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 2, "Provide username and slot");

        boolean onlyShow = args.length > 2 && args[2].equalsIgnoreCase("--onlyShow");
        PlayerEntity player = new PlayerFromNameArgumentParser(true).parse(args[0]);
        ItemStack item = getItem(player, args[1]);
        if (item == null) return;
        if (ShadowMain.client.interactionManager.hasCreativeInventory() && !onlyShow) {
            giveItem(item);
            message("Item copied.");
        } else {
            NbtCompound tag = item.getNbt();
            String nbt = tag == null ? "" : tag.asString();
            ShadowMain.client.keyboard.setClipboard(nbt);
            message("Nbt copied.");
        }

    }

    private ItemStack getItem(PlayerEntity player, String slot) {
        switch (slot.toLowerCase()) {
            case "hand":
                return player.getInventory().getMainHandStack();

            case "offhand":
                return player.getInventory().getStack(PlayerInventory.OFF_HAND_SLOT);

            case "head":
                return player.getInventory().getArmorStack(3);

            case "chest":
                return player.getInventory().getArmorStack(2);

            case "legs":
                return player.getInventory().getArmorStack(1);

            case "feet":
                return player.getInventory().getArmorStack(0);

            default:
                message("Invalid slot");
                return null;
        }
    }

    private void giveItem(ItemStack stack) {
        int slot = ShadowMain.client.player.getInventory().getEmptySlot();
        if (slot < 0) {
            message("Please clear a slot in your hotbar");
            return;
        }

        if (slot < 9) slot += 36;

        CreativeInventoryActionC2SPacket packet = new CreativeInventoryActionC2SPacket(slot, stack);
        ShadowMain.client.player.networkHandler.sendPacket(packet);
    }
}
