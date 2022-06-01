/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.ArgumentType;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.command.coloring.StaticArgumentServer;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Kill extends Command {

    boolean pendingBook = false;
    boolean sent2nd = false;
    int bookSlot = -1;

    public Kill() {
        super("Kill", "Makes another person have a sudden heart attack (requires creative)", "kill");
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!pendingBook) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            handlePacket(pe);
        });
        Events.registerEventHandler(EventType.NOCLIP_QUERY, event -> { // this also functions as a tick thing so eh
            if (pendingBook && bookSlot != -1) {
                assert ShadowMain.client.player != null;
                ShadowMain.client.player.getInventory().selectedSlot = bookSlot;
            }
        });
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.PLAYER, Objects.requireNonNull(ShadowMain.client.world).getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getGameProfile().getName()).toList().toArray(String[]::new)));
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
                success(String.format("Player's at X=%s,Y=%s,Z=%s, sending funny", Utils.Math.roundToDecimal(target.x, 1), Utils.Math.roundToDecimal(target.y, 1), Utils.Math.roundToDecimal(target.z, 1)));
                makeKillPotAt(new BlockHitResult(ShadowMain.client.player.getPos(), Direction.DOWN, new BlockPos(ShadowMain.client.player.getPos()), false), target);
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

    void makeKillPotAt(BlockHitResult bhr, Vec3d target) {
        Vec3d target1 = target.add(0, 2.7, 0);
        ItemStack s = Utils.generateItemStackWithMeta("{data: [], palette: [], EntityTag: {Item: {Count: 1b, id: \"minecraft:splash_potion\", tag: {CustomPotionEffects: [{ShowParticles: 1b, Duration: 20, Id: 6b, Amplifier: 125b}], Potion: \"minecraft:awkward\"}}, Pos: [" + target1.x + "d, " + target1.y + "d, " + target1.z + "d], Motion: [0d,-5d,0d], id: \"minecraft:potion\", LeftOwner: 1b}}", Items.BAT_SPAWN_EGG);
        assert ShadowMain.client.player != null;
        CreativeInventoryActionC2SPacket pack = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(ShadowMain.client.player.getInventory().selectedSlot), s);
        Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(pack);
        PlayerInteractBlockC2SPacket pack2 = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr);
        ShadowMain.client.getNetworkHandler().sendPacket(pack2);
        CreativeInventoryActionC2SPacket pack3 = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(ShadowMain.client.player.getInventory().selectedSlot), new ItemStack(Items.AIR));
        ShadowMain.client.getNetworkHandler().sendPacket(pack3);
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide username");
        assert ShadowMain.client.interactionManager != null;
        if (!ShadowMain.client.interactionManager.hasCreativeInventory()) {
            error("I cant give you a kill pot because you dont have a creative inv");
            return;
        }
        if (args[0].equals("*")) {
            message("Killing everyone in render distance");
            assert ShadowMain.client.world != null;
            for (AbstractClientPlayerEntity player : ShadowMain.client.world.getPlayers()) {
                if (player.equals(ShadowMain.client.player)) {
                    continue;
                }
                onExecute(new String[] { player.getGameProfile().getName() });
            }
            return;
        }
        HitResult hr = ShadowMain.client.crosshairTarget;
        assert hr != null;
        BlockHitResult bhr;
        if (!(hr instanceof BlockHitResult bhr1)) {
            bhr = new BlockHitResult(hr.getPos(), Direction.DOWN, new BlockPos(hr.getPos()), false);
        } else {
            bhr = bhr1;
        }
        String user = args[0];
        List<PlayerEntity> targets = new ArrayList<>();
        Vec3d p;
        String uname;
        if (user.startsWith("U")) {
            String uuidP = user.substring(1);
            try {
                UUID resolved = UUID.fromString(uuidP);
                assert ShadowMain.client.player != null;
                String n = "{pages:[\"{\\\"nbt\\\":\\\"Pos\\\",\\\"entity\\\":\\\"" + resolved + "\\\"}\"],title:\"0\",author:\"" + ShadowMain.client.player.getGameProfile().getName() + "\"}";
                ItemStack s = Utils.generateItemStackWithMeta(n, Items.WRITTEN_BOOK);
                pendingBook = true;
                bookSlot = ShadowMain.client.player.getInventory().selectedSlot;
                CreativeInventoryActionC2SPacket a = new CreativeInventoryActionC2SPacket(Utils.Inventory.slotIndexToId(ShadowMain.client.player.getInventory().selectedSlot), s);
                Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).sendPacket(a);
                message("Finding player coords...");
            } catch (Exception ignored) {
                error("UUID invalid");
            }
            return;
        } else {
            assert ShadowMain.client.world != null;
            for (Entity entity : ShadowMain.client.world.getEntities()) {
                if (entity instanceof PlayerEntity pe && pe.getGameProfile().getName().toLowerCase().contains(user.toLowerCase())) {
                    if (pe.getGameProfile().getName().equalsIgnoreCase(user)) { // direct match, we know who we want to fuck
                        targets.clear();
                        targets.add(pe);
                        break;
                    } else {
                        targets.add(pe);
                    }
                }
            }
            if (targets.size() == 0) {
                warn("Didnt find that player. Looking up globally...");
                UUID u = Utils.Players.getUUIDFromName(user);
                if (u == null) {
                    error("Couldn't find user's uuid. If you know it, run the command again with \"U(uuid)\"");
                    return;
                }
                onExecute(new String[] { "U" + u });
                return;
            }
            if (targets.size() > 1) {
                error("There are multiple players with that name. Be more specific");
                for (PlayerEntity target : targets) {
                    message(" - " + target.getGameProfile().getName().toLowerCase().replaceAll(user.toLowerCase(), "§c" + user.toLowerCase() + "§r"));
                }
            }
            PlayerEntity target = targets.get(0);
            p = target.getPos();
            uname = target.getGameProfile().getName();
        }
        makeKillPotAt(bhr, p);
        success("Killed " + uname);
    }
}
