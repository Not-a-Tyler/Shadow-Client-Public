/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.grief;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AutoFireball extends Module {

    final DoubleSetting radius = this.config.create(new DoubleSetting.Builder(15).max(100).min(10).name("Radius").description("The radius to search in").get());
    final Block[] blocks = new Block[] { Blocks.COBBLESTONE, Blocks.GLASS, Blocks.GLASS_PANE, Blocks.OAK_DOOR, Blocks.IRON_DOOR, Blocks.BRICKS, Blocks.OAK_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.WHITE_WOOL, Blocks.BLACK_WOOL, Blocks.BARREL, Blocks.CHEST, Blocks.CRAFTING_TABLE, Blocks.FURNACE };
    final BlockPos walkman = new BlockPos(0, 0, 0);
    final List<BlockPos> targets = new ArrayList<>();

    public AutoFireball() {
        super("AutoFireball", "Automatically nuke your surroundings with fireballs", ModuleType.MISC);
    }

    public static void raycast(BlockPos destination) {
        ItemStack item = new ItemStack(Items.COW_SPAWN_EGG, 1);
        ItemStack before = ShadowMain.client.player.getMainHandStack();
        try {
            item.setNbt(StringNbtReader.parse("{EntityTag:{id:\"minecraft:end_crystal\",ShowBottom:0b,BeamTarget:{X:" + destination.getX() + ",Y:" + destination.getY() + ",Z:" + destination.getZ() + "}}}"));
        } catch (Exception ignored) {
        }
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, item));
        ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(ShadowMain.client.player.getPos().add(new Vec3d(0, -2, 0)), Direction.UP, ShadowMain.client.player.getBlockPos().offset(Direction.DOWN, 2), false)));
        ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, before));
    }

    @Override
    public void tick() {

    }

    private double distanceToBlocks(BlockPos bl, BlockPos b) {
        Vec3d a = new Vec3d(bl.getX(), bl.getY(), bl.getZ());
        Vec3d bc = new Vec3d(b.getX(), b.getY(), b.getZ());
        return a.distanceTo(bc);
    }

    @Override
    public void enable() {
        targets.clear();
        Utils.Logging.message("Working...");
        BlockPos before = ShadowMain.client.player.getBlockPos();
        new Thread(() -> {
            int l = (int) Math.round(radius.getValue()) * 2;
            for (int x = -l; x < l; x++)
                for (int y = -l; y < l; y++)
                    for (int z = -l; z < l; z++) {
                        BlockPos pos = before.add(new BlockPos(x, y, z));
                        Block block = ShadowMain.client.world.getBlockState(pos).getBlock();
                        for (Block b : blocks) {
                            if (b.equals(block)) {
                                boolean executebreak = false;
                                for (BlockPos bl : new ArrayList<>(targets)) {
                                    if (distanceToBlocks(bl, pos) < 10) {
                                        executebreak = true;
                                    }
                                }
                                if (!executebreak) {
                                    targets.add(pos);
                                }
                            }
                        }
                    }
            ItemStack b4 = ShadowMain.client.player.getMainHandStack();
            for (BlockPos nuke : new ArrayList<>(targets)) {
                ItemStack fireball = new ItemStack(Items.BLAZE_SPAWN_EGG, 1);
                try {
                    fireball.setNbt(StringNbtReader.parse("{EntityTag:{id:\"minecraft:fireball\",ExplosionPower:25b,Pos:[" + nuke.getX() + ".0," + nuke.getY() + ".9," + nuke.getZ() + ".0],power:[0.0,-1.0,0.0]}}"));
                } catch (Exception ignored) {
                }
                ShadowMain.client.player.swingHand(Hand.MAIN_HAND);
                ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, fireball));
                ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(ShadowMain.client.player.getPos(), Direction.UP, ShadowMain.client.player.getBlockPos(), true)));
                Utils.sleep(75);
                raycast(nuke);
                Utils.sleep(75);
            }
            ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, b4));
            this.setEnabled(false);
        }).start();
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
        for (BlockPos globalpos : new ArrayList<>(targets)) {
            Vec3d vp = new Vec3d(globalpos.getX() - 1.5, globalpos.getY() - 1.5, globalpos.getZ() - 1.5);
            Renderer.R3D.renderFilled(vp, new Vec3d(3, 3, 3), new Color(53, 53, 53, 100), matrices);
        }
        Vec3d vp = new Vec3d(walkman.getX() - 1.5, walkman.getY() - 1.5, walkman.getZ() - 1.5);
        Renderer.R3D.renderFilled(vp, new Vec3d(3, 3, 3), new Color(255, 255, 255, 255), matrices);
    }

    @Override
    public void onHudRender() {

    }
}
