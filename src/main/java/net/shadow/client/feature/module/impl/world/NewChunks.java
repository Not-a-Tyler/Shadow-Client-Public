/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NewChunks extends Module {

    private static final Direction[] skipDirs = new Direction[]{Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH};
    private final Set<ChunkPos> newChunks = Collections.synchronizedSet(new HashSet<>());
    private final Set<ChunkPos> oldChunks = Collections.synchronizedSet(new HashSet<>());

    public NewChunks() {
        super("NewChunks", "Detect newly generated chunks", ModuleType.WORLD);
    }

    @EventListener(type=EventType.PACKET_RECEIVE)
    void onRecievePacket(PacketEvent event){
        Direction[] searchDirs = new Direction[]{Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.UP};

        if (event.getPacket() instanceof ChunkDeltaUpdateS2CPacket packet) {

            packet.visitUpdates((pos, state) -> {
                if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
                    ChunkPos chunkPos = new ChunkPos(pos);

                    for (Direction dir : searchDirs) {
                        if (client.world.getBlockState(pos.offset(dir)).getFluidState().isStill() && !oldChunks.contains(chunkPos)) {
                            newChunks.add(chunkPos);
                            return;
                        }
                    }
                }
            });
        } else if (event.getPacket() instanceof BlockUpdateS2CPacket packet) {

            if (!packet.getState().getFluidState().isEmpty() && !packet.getState().getFluidState().isStill()) {
                ChunkPos chunkPos = new ChunkPos(packet.getPos());

                for (Direction dir : searchDirs) {
                    if (client.world.getBlockState(packet.getPos().offset(dir)).getFluidState().isStill() && !oldChunks.contains(chunkPos)) {
                        newChunks.add(chunkPos);
                        return;
                    }
                }
            }
        } else if (event.getPacket() instanceof ChunkDataS2CPacket packet && client.world != null) {

            ChunkPos pos = new ChunkPos(packet.getX(), packet.getZ());

            if (!newChunks.contains(pos) && client.world.getChunkManager().getChunk(packet.getX(), packet.getZ()) == null) {
                WorldChunk chunk = new WorldChunk(client.world, pos);
                chunk.loadFromPacket(null, new NbtCompound(), packet.getChunkData().getBlockEntities(packet.getX(), packet.getZ()));

                for (int x = 0; x < 16; x++) {
                    for (int y = client.world.getBottomY(); y < client.world.getTopY(); y++) {
                        for (int z = 0; z < 16; z++) {
                            FluidState fluid = chunk.getFluidState(x, y, z);

                            if (!fluid.isEmpty() && !fluid.isStill()) {
                                oldChunks.add(pos);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
        newChunks.clear();
        oldChunks.clear();
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        synchronized(oldChunks){
            for (ChunkPos c : oldChunks) {
                if (client.getCameraEntity().getBlockPos().isWithinDistance(c.getStartPos(), 4096)) {
                    Renderer.R3D.renderFilled(new Vec3d(c.getStartX(), 0, c.getStartZ()), new Vec3d(16, 1, 16), new Color(0, 0, 255, 255), matrices);
                }
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}
