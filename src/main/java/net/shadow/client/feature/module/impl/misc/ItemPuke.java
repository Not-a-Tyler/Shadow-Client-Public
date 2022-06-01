/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Random;

public class ItemPuke extends Module {

    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(2).min(1).max(30).name("Speed").description("How fast to drop items").get());
    private final Random r = new Random();

    public ItemPuke() {
        super("ItemPuke", "Throw out items really fast in creative", ModuleType.MISC);
    }

    @Override
    public void tick() {
        for (int i = 0; i < speed.getValue(); i++) {
            ItemStack j = new ItemStack(Registry.ITEM.get(Registry.ITEM.getRandom(r).orElseThrow().getKey().orElseThrow().getValue()), 64);
            client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(client.player.getInventory().selectedSlot + 36, j));
            client.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.DROP_ALL_ITEMS, new BlockPos(0, 0, 0), Direction.UP));
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
