/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.misc;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.BlockRenderEvent;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class Test extends Module {
    static final Block searchTerm = Blocks.NETHER_PORTAL;
    final CopyOnWriteArrayList<BlockPos> discovered = new CopyOnWriteArrayList<>();

    public Test() {
        super("Test", "Testing stuff with the client, can be ignored", ModuleType.MISC);
        //        Events.registerEventHandlerClass(this);
    }

    @EventListener(type = EventType.BLOCK_RENDER)
    @SuppressWarnings("unused")
    void onBlockRender(BlockRenderEvent event) {
        if (!this.isEnabled()) return;
        BlockPos b = new BlockPos(event.getPosition());
        boolean listContains = discovered.stream().anyMatch(blockPos -> blockPos.equals(b));
        if (event.getBlockState().getBlock() == searchTerm) {
            if (!listContains) {
                discovered.add(b);
            }
        } else if (listContains) {
            discovered.removeIf(blockPos -> blockPos.equals(b));
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
        for (BlockPos bruh : discovered) {
            Renderer.R3D.renderEdged(matrices, Vec3d.of(bruh), new Vec3d(1, 1, 1), Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 100).darker(), Renderer.Util.modify(Utils.getCurrentRGB(), -1, -1, -1, 255));
        }
    }

    @Override
    public void onHudRender() {

    }

    @Override
    public void tick() {
        discovered.removeIf(blockPos -> client.world.getBlockState(blockPos).getBlock() != searchTerm);
        //        client.interactionManager.clickSlot(0, 0, 0, SlotActionType.QUICK_MOVE, client.player);
    }
}
