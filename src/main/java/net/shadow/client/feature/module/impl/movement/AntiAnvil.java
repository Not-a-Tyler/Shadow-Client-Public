/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class AntiAnvil extends Module {
    public AntiAnvil() {
        super("AntiAnvil", "Prevents you from getting anvil damage", ModuleType.MOVEMENT);
    }

    @Override
    public void tick() {
        //        Vec3d currentPos = CoffeeClientMain.client.player.getPos();
        BlockPos currentPos = Objects.requireNonNull(ShadowMain.client.player).getBlockPos();
        Vec3d ppos = ShadowMain.client.player.getPos();
        List<Entity> anvils = StreamSupport.stream(Objects.requireNonNull(ShadowMain.client.world).getEntities().spliterator(), false).filter(entity -> {
            if (entity instanceof FallingBlockEntity e) {
                Block bs = e.getBlockState().getBlock();
                return bs == Blocks.ANVIL || bs == Blocks.CHIPPED_ANVIL || bs == Blocks.DAMAGED_ANVIL;
            }
            return false;
        }).toList();
        for (Entity anvil : anvils) {
            Vec3d anvilPos = anvil.getPos();
            BlockPos anvilBp = anvil.getBlockPos();
            if (anvilBp.getX() == currentPos.getX() && anvilBp.getZ() == currentPos.getZ()) {
                double yDist = anvilPos.y - ppos.y;
                if (yDist > 0 && yDist < -anvil.getVelocity().y * 2) { // anvil is 0-1 blocks above our feet
                    PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 1, ppos.z, false);
                    Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(p);
                }
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
