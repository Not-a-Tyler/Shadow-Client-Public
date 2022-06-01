/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;

public class InteractCrash extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Block).name("Mode").description("How to interact").get());
    final DoubleSetting repeat = this.config.create(new DoubleSetting.Builder(5).min(1).max(100).name("Power").description("How much power to attack with").get());

    public InteractCrash() {
        super("InteractCrash", "Crash using interaction packets", ModuleType.CRASH);
        //        Events.registerEventHandlerClass(this);
    }

    @Override
    public void tick() {
        switch (mode.getValue()) {
            case Block -> {
                BlockHitResult bhr = (BlockHitResult) client.crosshairTarget;
                if (client.world.getBlockState(bhr.getBlockPos()).isAir()) return;
                for (int i = 0; i < repeat.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr));
                }
            }

            case Item -> {
                for (int i = 0; i < repeat.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND));
                }
            }

            case Entity -> {
                Entity target;
                if (!(client.crosshairTarget instanceof EntityHitResult)) {
                    return;
                }
                target = ((EntityHitResult) client.crosshairTarget).getEntity();
                for (int i = 0; i < repeat.getValue(); i++) {
                    client.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(target, false, Hand.MAIN_HAND));
                }
            }
        }
    }


    @EventListener(type = EventType.PACKET_RECEIVE)
    void onGotPacket(PacketEvent event) {
        if (!this.isEnabled()) return;
        if (event.getPacket() instanceof OpenScreenS2CPacket packet) {
            event.setCancelled(true);
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
        Block, Item, Entity
    }
}
