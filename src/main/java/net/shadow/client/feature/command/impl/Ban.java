/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.PlayerFromNameArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;

public class Ban extends Command {
    public Ban() {
        super("Ban", "Ban people from re-joining the server", "ban", "block");
    }


    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        if (index == 0)
            return new PossibleArgument(ArgumentType.PLAYER, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new));
        return super.getSuggestionsWithType(index, args);
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("Notch", "Player123");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide ban target's username");
        PlayerFromNameArgumentParser parser = new PlayerFromNameArgumentParser(true);
        String name = Utils.Players.completeName(args[0]);
        int[] player = Utils.Players.decodeUUID(parser.parse(name).getUuid());

        ItemStack ban = new ItemStack(Items.ARMOR_STAND, 1);
        message("Created Ban Stand for " + name);
        try {
            ban.setNbt(StringNbtReader.parse("{EntityTag:{UUID:[I;" + player[0] + "," + player[1] + "," + player[2] + "," + player[3] + "],ArmorItems:[{},{},{},{id:\"minecraft:player_head\",Count:1b,tag:{SkullOwner:\"" + name + "\"}}]}}"));
        } catch (Exception ignored) {
        }
        ban.setCustomName(Text.of(name));
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, ban));
    }
}
