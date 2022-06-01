/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.examples.ExampleServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.event.EventListener;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.event.events.base.Event;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;
import java.util.UUID;

public class Find extends Command {

    boolean pendingBook = false;
    boolean sent2nd = false;
    int bookSlot = -1;

    public Find() {
        super("Find", "Nocom 2 (requires creative)", "find", "findPlayer");
        Events.registerEventHandlerClass(this);
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return ExampleServer.getPlayerNames();
    }

    @EventListener(type = EventType.PACKET_RECEIVE)
    void pRecv(PacketEvent pe) {
        if (!pendingBook) {
            return;
        }
        handlePacket(pe);
    }

    @EventListener(type = EventType.NOCLIP_QUERY)
    void tick(Event e) {
        if (pendingBook && bookSlot != -1) {
            assert ShadowMain.client.player != null;
            ShadowMain.client.player.getInventory().selectedSlot = bookSlot;
        }
    }

    void handlePacket(PacketEvent pe) {
        if (pe.getPacket() instanceof OpenWrittenBookS2CPacket) {
            if (!sent2nd) {
                pe.setCancelled(true);
                sent2nd = true;
                return;
            }
            assert ShadowMain.client.player != null;
            ItemStack current = ShadowMain.client.player.getInventory().getMainHandStack();
            NbtCompound c = current.getOrCreateNbt();
            if (c.contains("pages", NbtCompound.LIST_TYPE)) {
                NbtList l = c.getList("pages", NbtCompound.STRING_TYPE);
                NbtString posComp = (NbtString) l.get(0);
                String value = posComp.asString();
                JsonObject root = JsonParser.parseString(value).getAsJsonObject();
                if (root.get("text") == null || root.get("text").getAsString().isEmpty()) {
                    error("Couldn't find player, is the dude online?");
                    CreativeInventoryActionC2SPacket pack3 = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(bookSlot), new ItemStack(Items.AIR));
                    Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(pack3);
                    pendingBook = sent2nd = false;
                    bookSlot = -1;
                    pe.setCancelled(true);
                    return;
                }
                String m = root.get("text").getAsString();
                m = m.replaceAll("\\[", "").replaceAll("]", "");
                String[] v = m.split(",");
                Vec3d target = new Vec3d(Double.parseDouble(v[0]), Double.parseDouble(v[1]), Double.parseDouble(v[2])); // jesus fucking christ
                pendingBook = sent2nd = false;
                bookSlot = -1;
                success(String.format("Found player at %s %s %s", Utils.Math.roundToDecimal(target.x, 1), Utils.Math.roundToDecimal(target.y, 1), Utils.Math.roundToDecimal(target.z, 1)));
            } else {
                error("Couldn't find player, is the dude online?");
                CreativeInventoryActionC2SPacket pack3 = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(bookSlot), new ItemStack(Items.AIR));
                Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(pack3);
                pendingBook = sent2nd = false;
                bookSlot = -1;
            }
            pe.setCancelled(true);
        } else if (pe.getPacket() instanceof ScreenHandlerSlotUpdateS2CPacket packet) {
            if (packet.getItemStack().getItem() == Items.WRITTEN_BOOK) {
                Utils.TickManager.runInNTicks(5, () -> Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND)));
            }
        }
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "(uuid)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide player username");
        if (!Objects.requireNonNull(ShadowMain.client.interactionManager).hasCreativeInventory()) {
            error("Cant find the player, need GMC");
            return;
        }
        UUID u = Utils.Players.getUUIDFromName(args[0]);
        if (u == null) {
            error("Couldn't find user's uuid.");
            return;
        }
        try {
            assert ShadowMain.client.player != null;
            String n = "{pages:[\"{\\\"nbt\\\":\\\"Pos\\\",\\\"entity\\\":\\\"" + u + "\\\"}\"],title:\"0\",author:\"" + ShadowMain.client.player.getGameProfile().getName() + "\"}";
            ItemStack s = Utils.generateItemStackWithMeta(n, Items.WRITTEN_BOOK);
            pendingBook = true;
            bookSlot = ShadowMain.client.player.getInventory().selectedSlot;
            CreativeInventoryActionC2SPacket a = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(ShadowMain.client.player.getInventory().selectedSlot), s);
            Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(a);
            message("Finding player coords...");
        } catch (Exception ignored) {
            error("UUID invalid");
        }

    }
}
