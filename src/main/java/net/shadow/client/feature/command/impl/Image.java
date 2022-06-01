/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
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

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Image extends Command {
    final String block = "█";
    BufferedImage imageToBuild;
    boolean real;

    public Image() {
        super("Image", "Apply an image to various text mediums", "image", "img");
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!real) return;
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof GameMessageS2CPacket p) {
                if (p.getMessage().getString().contains("Command set:")) {
                    event.setCancelled(true);
                }
            }
        });
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        return new ExamplesEntry("chat https://i.pinimg.com/originals/3c/e5/3e/3ce53e9d93949c2fb2372ffcf0751aac.png 60", "lore https://i.pinimg.com/originals/3c/e5/3e/3ce53e9d93949c2fb2372ffcf0751aac.png 100", "help");
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, "chat", "book", "lore"), new PossibleArgument(ArgumentType.STRING, "(url)"), new PossibleArgument(ArgumentType.NUMBER, "(size)"));
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        validateArgumentsLength(args, 1, "Provide action");
        if (args[0].equalsIgnoreCase("help")) {
            message("Modes:");
            message(">image chat");
            message(">image book");
            message(">image lore");
            return;
        }
        validateArgumentsLength(args, 3, "Provide action, url and size");
        switch (args[0].toLowerCase()) {
            case "chat" -> new Thread(() -> {
                try {
                    real = true;
                    loadImage(args[1], Integer.parseInt(args[2]));
                    int max = imageToBuild.getHeight();
                    for (int index = 0; index < max; index++) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("[");
                        for (int i = 0; i < imageToBuild.getWidth(); i++) {
                            int r = imageToBuild.getRGB(i, index);
                            int rP = r & 0xFFFFFF | 0xF000000;
                            builder.append("{\"text\":\"").append(block).append("\",\"color\":\"#").append(Integer.toString(rP, 16).substring(1)).append("\"},");
                        }
                        String mc = builder.substring(0, builder.length() - 1) + "]";
                        Utils.sleep(50);
                        ShadowMain.client.player.networkHandler.sendPacket(new UpdateCommandBlockC2SPacket(((BlockHitResult) ShadowMain.client.crosshairTarget).getBlockPos(), "REST", CommandBlockBlockEntity.Type.REDSTONE, false, false, false));
                        Utils.sleep(50);
                        ShadowMain.client.player.networkHandler.sendPacket(new UpdateCommandBlockC2SPacket(((BlockHitResult) ShadowMain.client.crosshairTarget).getBlockPos(), "/execute run tellraw @a " + mc, CommandBlockBlockEntity.Type.REDSTONE, false, false, true));
                    }
                } catch (Exception e) {
                    message("ChatPrinter");
                }
                Utils.sleep(2000);
                real = false;
            }).start();
            case "lore" -> {
                ItemStack item = ShadowMain.client.player.getMainHandStack();
                StringBuilder page = new StringBuilder();
                loadImage(args[1], Integer.parseInt(args[2]));
                int max = imageToBuild.getHeight();
                for (int index = 0; index < max; index++) {
                    StringBuilder lamo = new StringBuilder();
                    for (int i = 0; i < imageToBuild.getWidth(); i++) {
                        int r = imageToBuild.getRGB(i, index);
                        int hex = r & 0xFFFFFF | 0xF000000;
                        lamo.append("{\"text\":\"").append(block).append("\",\"color\":\"#").append(Integer.toString(hex, 16).substring(1)).append("\",\"italic\":false},");
                    }
                    String lamopage = lamo.substring(0, lamo.length() - 1);
                    page.append("'[").append(lamopage).append("]'").append(",");
                }
                String loader = page.substring(0, page.length() - 1);
                try {
                    item.getOrCreateNbt().copyFrom(StringNbtReader.parse("{display:{Lore:[" + loader + "]}}"));
                } catch (Exception ignored) {
                }
                ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, item));
                message("ImageBook");
            }
            case "book" -> {
                ItemStack book = new ItemStack(Items.WRITTEN_BOOK, 1);
                StringBuilder pager = new StringBuilder();
                loadImage(args[1]);
                int ma2x = imageToBuild.getHeight();
                for (int index = 0; index < ma2x; index++) {
                    for (int i = 0; i < imageToBuild.getWidth(); i++) {
                        int r = imageToBuild.getRGB(i, index);
                        int hex = r & 0xFFFFFF | 0xF000000;
                        pager.append("{\"text\":\"").append(block).append("\",\"color\":\"#").append(Integer.toString(hex, 16).substring(1)).append("\"},");
                    }
                    pager.append("{\"text\":\"\\\\n\"},");
                }
                String loaderstr = pager.substring(0, pager.length() - 1);
                try {
                    book.getOrCreateNbt().copyFrom(StringNbtReader.parse("{title:\"\",author:\"ImageBook\",pages:['[" + loaderstr + "]']}"));
                } catch (Exception ignored) {
                }
                ShadowMain.client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + ShadowMain.client.player.getInventory().selectedSlot, book));
                message("ImageBook");
            }
        }
    }

    public void loadImage(String imageurl, int size) {
        try {
            URL u = new URL(imageurl);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:88.0) Gecko/20100101 Firefox/88.0");
            huc.connect();
            InputStream is = huc.getInputStream();
            BufferedImage loadedImage = ImageIO.read(is);
            double scale = (double) loadedImage.getWidth() / (double) size;
            imageToBuild = resize(loadedImage, (int) (loadedImage.getWidth() / scale), (int) (loadedImage.getHeight() / scale));
            message("Loaded Image into memory");
            huc.disconnect();
        } catch (Exception ignored) {
            message("Failed to Loaded Image into memory");
        }
    }

    public void loadImage(String imageurl) {
        try {
            URL u = new URL(imageurl);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:88.0) Gecko/20100101 Firefox/88.0");
            huc.connect();
            InputStream is = huc.getInputStream();
            BufferedImage loadedImage = ImageIO.read(is);
            double scalew = (double) loadedImage.getWidth() / 12;
            double scaleh = (double) loadedImage.getHeight() / 15;
            imageToBuild = resize(loadedImage, (int) (loadedImage.getWidth() / scalew), (int) (loadedImage.getHeight() / scaleh));
            huc.disconnect();
        } catch (Exception ignored) {
            message("ImageLoader");
        }
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
