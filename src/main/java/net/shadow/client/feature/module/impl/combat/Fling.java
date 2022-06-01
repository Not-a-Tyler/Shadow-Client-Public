/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils;

public class Fling extends Module {


    final DoubleSetting delay = this.config.create(new DoubleSetting.Builder(250).min(1).max(500).precision(0).name("Delay").description("The delay before going back down").get());
    final DoubleSetting updist = this.config.create(new DoubleSetting.Builder(3).min(1).max(3).precision(2).name("Power").description("The power of the fling").get());
    boolean not = true;


    public Fling() {
        super("Fling", "Fling players in the air", ModuleType.COMBAT);
        //        Events.registerEventHandlerClass(this);
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

    @EventListener(type = EventType.PACKET_SEND)
    void sendPacket(PacketEvent event) {
        if (!this.isEnabled()) return;
        if (!not) return;
        if (event.getPacket() instanceof PlayerInteractItemC2SPacket) {
            if (client.player.getInventory().getMainHandStack().getItem() == Items.FISHING_ROD && (client.player.fishHook != null)) {
                if (client.player.fishHook.isRemoved()) return;
                client.player.setVelocity(Vec3d.ZERO);
                event.setCancelled(true);
                new Thread(() -> {
                    double staticy = client.player.getY();
                    for (int i = 0; i < updist.getValue(); i++) {
                        staticy = staticy + 9;
                        Utils.sleep(5);
                        client.player.setVelocity(Vec3d.ZERO);
                        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), staticy, client.player.getZ(), true));
                    }
                    Utils.sleep(delay.getValue().longValue());
                    not = false;
                    client.player.networkHandler.getConnection().send(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND));
                    not = true;
                    Utils.sleep(delay.getValue().longValue());
                    for (int i = 0; i < updist.getValue(); i++) {
                        staticy = staticy - 9;
                        Utils.sleep(5);
                        client.player.setVelocity(Vec3d.ZERO);
                        client.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(client.player.getX(), staticy, client.player.getZ(), true));
                    }
                }).start();
            }
        }
    }

}
