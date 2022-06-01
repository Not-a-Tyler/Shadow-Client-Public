/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.MouseEvent;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;

public class AirPlace extends Module {

    boolean enabled = false;

    public AirPlace() {
        super("AirPlace", "Places blocks in the air", ModuleType.MISC);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled()) return;
            if (enabled && ((MouseEvent) event).getButton() == 1 && ((MouseEvent) event).getAction() == 1) {
                if (ShadowMain.client.currentScreen != null) return;
                try {
                    if (!client.world.getBlockState(((BlockHitResult) ShadowMain.client.crosshairTarget).getBlockPos()).isAir())
                        return;
                    ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult) ShadowMain.client.crosshairTarget));
                    if ((client.player.getMainHandStack().getItem() instanceof BlockItem))
                        Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 255), Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 100).darker(), Vec3d.of(((BlockHitResult) ShadowMain.client.crosshairTarget).getBlockPos()), new Vec3d(1, 1, 1), 1000);
                    ShadowMain.client.player.swingHand(Hand.MAIN_HAND);
                    event.setCancelled(true);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        enabled = true;
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
