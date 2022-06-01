/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.argument.PlayerFromNameArgumentParser;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;

public class LinkWolf extends Command {
    public LinkWolf() {
        super("LinkWolf", "Link a wolf to a player", "linkWolf", "lWolf");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.PLAYER, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new)));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide username");
        if (!ShadowMain.client.player.getAbilities().creativeMode) {
            error("You must be in creative mode");
            return;
        }
        PlayerEntity player = new PlayerFromNameArgumentParser(true).parse(args[0]);
        int[] ub = Utils.Players.decodeUUID(player.getUuid());
        NbtCompound tag = new NbtCompound();
        try {
            tag = StringNbtReader.parse("{EntityTag:{CustomNameVisible:0b,Owner:[I;" + ub[0] + "," + ub[1] + "," + ub[2] + "," + ub[3] + "],Sitting:1b}}");
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        Item item = Registry.ITEM.get(new Identifier("wolf_spawn_egg"));
        ItemStack handitem = ShadowMain.client.player.getMainHandStack();
        ItemStack stack = new ItemStack(item, 1);
        stack.setNbt(tag);
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, stack));
        ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult) ShadowMain.client.crosshairTarget));
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, handitem));
        message("Spawned linked wolf for " + player.getGameProfile().getName());
    }
}
