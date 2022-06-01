/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.exception.CommandException;

public class AsConsole extends Command {
    public AsConsole() {
        super("AsConsole", "Run a command as console via command blocks", "asConsole", "runas");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return new PossibleArgument(ArgumentType.STRING, "(command)");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide the command to use");
        ItemStack console = new ItemStack(Items.COMMAND_BLOCK, 1);
        String command = String.join(" ", args);
        ItemStack b4 = ShadowMain.client.player.getMainHandStack();
        BlockHitResult b = (BlockHitResult) ShadowMain.client.crosshairTarget;
        BlockPos cbp = b.getBlockPos();
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(ShadowMain.client.player.getInventory().selectedSlot + 36, console));
        ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult) ShadowMain.client.crosshairTarget));
        ShadowMain.client.player.networkHandler.sendPacket(new UpdateCommandBlockC2SPacket(cbp, command, CommandBlockBlockEntity.Type.REDSTONE, false, false, false));
        ShadowMain.client.player.networkHandler.sendPacket(new UpdateCommandBlockC2SPacket(cbp, command, CommandBlockBlockEntity.Type.REDSTONE, false, false, true));
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            ShadowMain.client.interactionManager.attackBlock(cbp, Direction.DOWN);
        }).start();
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, b4));
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("say hello", "tp @e 0 0 0");
    }
}
