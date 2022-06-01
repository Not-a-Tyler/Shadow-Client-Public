/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.combat;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.feature.module.impl.movement.NoFall;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

public class Criticals extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Packet).name("Mode").description("How to deal crits").get());

    public Criticals() {
        super("Criticals", "Makes you deal a perfect 10/10 crit every time", ModuleType.COMBAT);
        Events.registerEventHandler(EventType.PACKET_SEND, event1 -> {
            PacketEvent event = (PacketEvent) event1;
            if (ShadowMain.client.player == null || ShadowMain.client.getNetworkHandler() == null) {
                return;
            }
            if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && this.isEnabled()) {
                Vec3d ppos = ShadowMain.client.player.getPos();
                ModuleRegistry.getByClass(NoFall.class).enabled = false; // disable nofall modifying packets when we send these
                switch (mode.getValue()) {
                    case Packet -> {
                        PlayerMoveC2SPacket.PositionAndOnGround p1 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.2, ppos.z, true);
                        PlayerMoveC2SPacket.PositionAndOnGround p2 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p3 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.000011, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p4 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y, ppos.z, false);
                        ShadowMain.client.getNetworkHandler().sendPacket(p1);
                        ShadowMain.client.getNetworkHandler().sendPacket(p2);
                        ShadowMain.client.getNetworkHandler().sendPacket(p3);
                        ShadowMain.client.getNetworkHandler().sendPacket(p4);
                    }
                    case TpHop -> {
                        PlayerMoveC2SPacket.PositionAndOnGround p5 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.02, ppos.z, false);
                        PlayerMoveC2SPacket.PositionAndOnGround p6 = new PlayerMoveC2SPacket.PositionAndOnGround(ppos.x, ppos.y + 0.01, ppos.z, false);
                        ShadowMain.client.getNetworkHandler().sendPacket(p5);
                        ShadowMain.client.getNetworkHandler().sendPacket(p6);
                    }
                }
                ModuleRegistry.getByClass(NoFall.class).enabled = true; // re-enable nofall
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

    //    final MultiValue mode = (MultiValue) this.config.create("Mode", "packet", "packet", "tphop").description("The mode");
    public enum Mode {
        Packet, TpHop
    }
}

