/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.command;

import net.shadow.client.feature.addon.Addon;
import net.shadow.client.feature.command.exception.CommandException;
import net.shadow.client.feature.command.impl.ApplyVel;
import net.shadow.client.feature.command.impl.AsConsole;
import net.shadow.client.feature.command.impl.Author;
import net.shadow.client.feature.command.impl.Ban;
import net.shadow.client.feature.command.impl.Bind;
import net.shadow.client.feature.command.impl.Boot;
import net.shadow.client.feature.command.impl.CheckCmd;
import net.shadow.client.feature.command.impl.ClearInventory;
import net.shadow.client.feature.command.impl.Config;
import net.shadow.client.feature.command.impl.ConfigUtils;
import net.shadow.client.feature.command.impl.Crash;
import net.shadow.client.feature.command.impl.Damage;
import net.shadow.client.feature.command.impl.Drop;
import net.shadow.client.feature.command.impl.EVclip;
import net.shadow.client.feature.command.impl.Effect;
import net.shadow.client.feature.command.impl.Equip;
import net.shadow.client.feature.command.impl.FakeItem;
import net.shadow.client.feature.command.impl.Find;
import net.shadow.client.feature.command.impl.FloodLuckperms;
import net.shadow.client.feature.command.impl.ForEach;
import net.shadow.client.feature.command.impl.ForceOP;
import net.shadow.client.feature.command.impl.Gamemode;
import net.shadow.client.feature.command.impl.HClip;
import net.shadow.client.feature.command.impl.Help;
import net.shadow.client.feature.command.impl.Hologram;
import net.shadow.client.feature.command.impl.Image;
import net.shadow.client.feature.command.impl.Inject;
import net.shadow.client.feature.command.impl.Invsee;
import net.shadow.client.feature.command.impl.ItemData;
import net.shadow.client.feature.command.impl.ItemExploit;
import net.shadow.client.feature.command.impl.ItemSpoof;
import net.shadow.client.feature.command.impl.KickSelf;
import net.shadow.client.feature.command.impl.Kickall;
import net.shadow.client.feature.command.impl.Kill;
import net.shadow.client.feature.command.impl.LinkWolf;
import net.shadow.client.feature.command.impl.LogFlood;
import net.shadow.client.feature.command.impl.MessageSpam;
import net.shadow.client.feature.command.impl.Panic;
import net.shadow.client.feature.command.impl.PermissionLevel;
import net.shadow.client.feature.command.impl.Poof;
import net.shadow.client.feature.command.impl.RandomBook;
import net.shadow.client.feature.command.impl.Rename;
import net.shadow.client.feature.command.impl.Say;
import net.shadow.client.feature.command.impl.ServerCrash;
import net.shadow.client.feature.command.impl.SocketFlood;
import net.shadow.client.feature.command.impl.SocketKick;
import net.shadow.client.feature.command.impl.SpawnData;
import net.shadow.client.feature.command.impl.StopServer;
import net.shadow.client.feature.command.impl.Taco;
import net.shadow.client.feature.command.impl.Test;
import net.shadow.client.feature.command.impl.TitleLag;
import net.shadow.client.feature.command.impl.Toggle;
import net.shadow.client.feature.command.impl.VClip;
import net.shadow.client.feature.command.impl.ViewNbt;
import net.shadow.client.helper.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
    private static final List<Command> vanillaCommands = new ArrayList<>();
    private static final List<CustomCommandEntry> customCommands = new ArrayList<>();
    private static final List<Command> sharedCommands = new ArrayList<>();

    static {
        //        init();
        rebuildSharedCommands();
    }

    public static void registerCustomCommand(Addon addon, Command command) {
        for (CustomCommandEntry e : customCommands) {
            if (e.command.getClass() == command.getClass()) {
                throw new IllegalStateException("Command " + command.getClass().getSimpleName() + " already registered");
            }
        }
        customCommands.add(new CustomCommandEntry(addon, command));
        rebuildSharedCommands();
    }

    public static void clearCustomCommands(Addon addon) {
        customCommands.removeIf(customCommandEntry -> customCommandEntry.addon == addon);
        rebuildSharedCommands();
    }

    private static void rebuildSharedCommands() {
        sharedCommands.clear();
        sharedCommands.addAll(vanillaCommands);
        for (CustomCommandEntry customCommand : customCommands) {
            sharedCommands.add(customCommand.command);
        }
    }

    public static void init() {
        vanillaCommands.clear();
        vanillaCommands.add(new Toggle());
        vanillaCommands.add(new Config());
        vanillaCommands.add(new Gamemode());
        vanillaCommands.add(new Effect());
        vanillaCommands.add(new Hologram());
        vanillaCommands.add(new Help());
        vanillaCommands.add(new ForEach());
        vanillaCommands.add(new Drop());
        vanillaCommands.add(new Panic());
        vanillaCommands.add(new Rename());
        vanillaCommands.add(new ViewNbt());
        vanillaCommands.add(new Say());
        vanillaCommands.add(new ConfigUtils());
        vanillaCommands.add(new Kill());
        vanillaCommands.add(new Invsee());
        vanillaCommands.add(new Find());
        vanillaCommands.add(new FakeItem());
        vanillaCommands.add(new Taco());
        vanillaCommands.add(new Bind());
        vanillaCommands.add(new Test());
        vanillaCommands.add(new Kickall());
        vanillaCommands.add(new ItemExploit());
        vanillaCommands.add(new Inject());
        vanillaCommands.add(new ApplyVel());
        vanillaCommands.add(new AsConsole());
        vanillaCommands.add(new Author());
        vanillaCommands.add(new Ban());
        vanillaCommands.add(new Boot());
        vanillaCommands.add(new CheckCmd());
        vanillaCommands.add(new LogFlood());
        vanillaCommands.add(new PermissionLevel());
        vanillaCommands.add(new Crash());
        vanillaCommands.add(new Damage());
        vanillaCommands.add(new Equip());
        vanillaCommands.add(new EVclip());
        vanillaCommands.add(new FloodLuckperms());
        vanillaCommands.add(new ItemSpoof());
        vanillaCommands.add(new HClip());
        vanillaCommands.add(new Image());
        vanillaCommands.add(new ItemData());
        vanillaCommands.add(new KickSelf());
        vanillaCommands.add(new TitleLag());
        vanillaCommands.add(new LinkWolf());
        vanillaCommands.add(new Poof());
        vanillaCommands.add(new SpawnData());
        vanillaCommands.add(new StopServer());
        vanillaCommands.add(new VClip());
        vanillaCommands.add(new MessageSpam());
        vanillaCommands.add(new ClearInventory());
        vanillaCommands.add(new ForceOP());
        vanillaCommands.add(new ServerCrash());
        vanillaCommands.add(new RandomBook());
        vanillaCommands.add(new SocketKick());
        vanillaCommands.add(new SocketFlood());

        rebuildSharedCommands();
    }

    public static List<Command> getCommands() {
        return sharedCommands;
    }

    public static void execute(String command) {
        String[] spl = command.split(" +");
        String cmd = spl[0].toLowerCase();
        String[] args = Arrays.copyOfRange(spl, 1, spl.length);
        Command c = CommandRegistry.getByAlias(cmd);
        if (c == null) {
            Utils.Logging.error("Command \"" + cmd + "\" not found");
        } else {
            try {
                c.onExecute(args);
            } catch (CommandException cex) {
                Utils.Logging.error(cex.getMessage());
                if (cex.getPotentialFix() != null) Utils.Logging.error("Potential fix: " + cex.getPotentialFix());
            } catch (Exception e) {
                Utils.Logging.error("Error while running command " + command);
                e.printStackTrace();
            }
        }
    }

    public static Command getByAlias(String n) {
        for (Command command : getCommands()) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(n)) {
                    return command;
                }
            }
        }
        return null;
    }

    record CustomCommandEntry(Addon addon, Command command) {
    }
}
