/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module;


import net.shadow.client.ShadowMain;
import net.shadow.client.feature.addon.Addon;
import net.shadow.client.feature.module.impl.combat.AimAssist;
import net.shadow.client.feature.module.impl.combat.AutoAttack;
import net.shadow.client.feature.module.impl.combat.AutoTrap;
import net.shadow.client.feature.module.impl.combat.Criticals;
import net.shadow.client.feature.module.impl.combat.FireballDeflector;
import net.shadow.client.feature.module.impl.combat.Fling;
import net.shadow.client.feature.module.impl.combat.Killaura;
import net.shadow.client.feature.module.impl.combat.NoSwing;
import net.shadow.client.feature.module.impl.combat.Reach;
import net.shadow.client.feature.module.impl.combat.ReverseKnockback;
import net.shadow.client.feature.module.impl.combat.ShulkerDeflector;
import net.shadow.client.feature.module.impl.combat.TpRange;
import net.shadow.client.feature.module.impl.combat.Velocity;
import net.shadow.client.feature.module.impl.crash.AnimationCrash;
import net.shadow.client.feature.module.impl.crash.ArmorStandCrash;
import net.shadow.client.feature.module.impl.crash.BookInflaterCrash;
import net.shadow.client.feature.module.impl.crash.ClientCrasher;
import net.shadow.client.feature.module.impl.crash.CraftCrash;
import net.shadow.client.feature.module.impl.crash.EntityCrash;
import net.shadow.client.feature.module.impl.crash.ErrorCrash;
import net.shadow.client.feature.module.impl.crash.FlightCrash;
import net.shadow.client.feature.module.impl.crash.InteractCrash;
import net.shadow.client.feature.module.impl.crash.LecternCrash;
import net.shadow.client.feature.module.impl.crash.LoominaCrash;
import net.shadow.client.feature.module.impl.crash.MinehutCrash;
import net.shadow.client.feature.module.impl.crash.MoveCrash;
import net.shadow.client.feature.module.impl.crash.OOBCrash;
import net.shadow.client.feature.module.impl.crash.PacketCrash;
import net.shadow.client.feature.module.impl.crash.PaintingCrash;
import net.shadow.client.feature.module.impl.crash.RequestCrash;
import net.shadow.client.feature.module.impl.crash.SSRFCrash;
import net.shadow.client.feature.module.impl.crash.SwapCrash;
import net.shadow.client.feature.module.impl.exploit.AntiAntiXray;
import net.shadow.client.feature.module.impl.exploit.AntiRDI;
import net.shadow.client.feature.module.impl.exploit.BeaconSpoofer;
import net.shadow.client.feature.module.impl.exploit.BoatCrash;
import net.shadow.client.feature.module.impl.exploit.BoatFling;
import net.shadow.client.feature.module.impl.exploit.BrandSpoof;
import net.shadow.client.feature.module.impl.exploit.CarpetBomb;
import net.shadow.client.feature.module.impl.exploit.ChunkCrash;
import net.shadow.client.feature.module.impl.exploit.ConsoleSpammer;
import net.shadow.client.feature.module.impl.exploit.Equipper;
import net.shadow.client.feature.module.impl.exploit.FilterBypass;
import net.shadow.client.feature.module.impl.exploit.ImageLoader;
import net.shadow.client.feature.module.impl.exploit.InstaBow;
import net.shadow.client.feature.module.impl.exploit.OffhandCrash;
import net.shadow.client.feature.module.impl.exploit.PingSpoof;
import net.shadow.client.feature.module.impl.grief.Annihilator;
import net.shadow.client.feature.module.impl.grief.AutoFireball;
import net.shadow.client.feature.module.impl.grief.AutoIgnite;
import net.shadow.client.feature.module.impl.grief.AutoRun;
import net.shadow.client.feature.module.impl.grief.AutoTNT;
import net.shadow.client.feature.module.impl.grief.ControlPanel;
import net.shadow.client.feature.module.impl.grief.Decimator;
import net.shadow.client.feature.module.impl.grief.Explosion;
import net.shadow.client.feature.module.impl.grief.MapFuck;
import net.shadow.client.feature.module.impl.grief.MultiShot;
import net.shadow.client.feature.module.impl.misc.AdBlock;
import net.shadow.client.feature.module.impl.misc.AdSpammer;
import net.shadow.client.feature.module.impl.misc.AllowFormatCodes;
import net.shadow.client.feature.module.impl.misc.AntiCrash;
import net.shadow.client.feature.module.impl.misc.AntiOffhandCrash;
import net.shadow.client.feature.module.impl.misc.AntiPacketKick;
import net.shadow.client.feature.module.impl.misc.ClientSettings;
import net.shadow.client.feature.module.impl.misc.DiscordRPC;
import net.shadow.client.feature.module.impl.misc.InfChatLength;
import net.shadow.client.feature.module.impl.misc.ItemPuke;
import net.shadow.client.feature.module.impl.misc.MoreChatHistory;
import net.shadow.client.feature.module.impl.misc.NoPacketKick;
import net.shadow.client.feature.module.impl.misc.NoSRP;
import net.shadow.client.feature.module.impl.misc.NoTitles;
import net.shadow.client.feature.module.impl.misc.PortalGUI;
import net.shadow.client.feature.module.impl.misc.Spinner;
import net.shadow.client.feature.module.impl.misc.SuperCrossbow;
import net.shadow.client.feature.module.impl.misc.Test;
import net.shadow.client.feature.module.impl.misc.Timer;
import net.shadow.client.feature.module.impl.misc.XCarry;
import net.shadow.client.feature.module.impl.movement.AirJump;
import net.shadow.client.feature.module.impl.movement.AntiAnvil;
import net.shadow.client.feature.module.impl.movement.AutoElytra;
import net.shadow.client.feature.module.impl.movement.Backtrack;
import net.shadow.client.feature.module.impl.movement.Blink;
import net.shadow.client.feature.module.impl.movement.BlocksMCFlight;
import net.shadow.client.feature.module.impl.movement.BoatPhase;
import net.shadow.client.feature.module.impl.movement.Boost;
import net.shadow.client.feature.module.impl.movement.ClickTP;
import net.shadow.client.feature.module.impl.movement.EdgeJump;
import net.shadow.client.feature.module.impl.movement.EdgeSneak;
import net.shadow.client.feature.module.impl.movement.EntityFly;
import net.shadow.client.feature.module.impl.movement.Flight;
import net.shadow.client.feature.module.impl.movement.Hyperspeed;
import net.shadow.client.feature.module.impl.movement.IgnoreWorldBorder;
import net.shadow.client.feature.module.impl.movement.InventoryWalk;
import net.shadow.client.feature.module.impl.movement.Jesus;
import net.shadow.client.feature.module.impl.movement.LongJump;
import net.shadow.client.feature.module.impl.movement.MoonGravity;
import net.shadow.client.feature.module.impl.movement.NoFall;
import net.shadow.client.feature.module.impl.movement.NoJumpCool;
import net.shadow.client.feature.module.impl.movement.NoLevitation;
import net.shadow.client.feature.module.impl.movement.NoPush;
import net.shadow.client.feature.module.impl.movement.Phase;
import net.shadow.client.feature.module.impl.movement.Speed;
import net.shadow.client.feature.module.impl.movement.Sprint;
import net.shadow.client.feature.module.impl.movement.Step;
import net.shadow.client.feature.module.impl.movement.Swing;
import net.shadow.client.feature.module.impl.render.BlockHighlighting;
import net.shadow.client.feature.module.impl.render.CaveMapper;
import net.shadow.client.feature.module.impl.render.ChestHighlighter;
import net.shadow.client.feature.module.impl.render.ClickGUI;
import net.shadow.client.feature.module.impl.render.ESP;
import net.shadow.client.feature.module.impl.render.FakeHacker;
import net.shadow.client.feature.module.impl.render.FreeLook;
import net.shadow.client.feature.module.impl.render.Freecam;
import net.shadow.client.feature.module.impl.render.Fullbright;
import net.shadow.client.feature.module.impl.render.Hud;
import net.shadow.client.feature.module.impl.render.ItemByteSize;
import net.shadow.client.feature.module.impl.render.MouseEars;
import net.shadow.client.feature.module.impl.render.NameTags;
import net.shadow.client.feature.module.impl.render.NoLiquidFog;
import net.shadow.client.feature.module.impl.render.Radar;
import net.shadow.client.feature.module.impl.render.ShowTntPrime;
import net.shadow.client.feature.module.impl.render.Spotlight;
import net.shadow.client.feature.module.impl.render.TabGui;
import net.shadow.client.feature.module.impl.render.TargetHud;
import net.shadow.client.feature.module.impl.render.Theme;
import net.shadow.client.feature.module.impl.render.ToolsScreen;
import net.shadow.client.feature.module.impl.render.Tracers;
import net.shadow.client.feature.module.impl.render.Trail;
import net.shadow.client.feature.module.impl.render.Zoom;
import net.shadow.client.feature.module.impl.world.AirPlace;
import net.shadow.client.feature.module.impl.world.AnyPlacer;
import net.shadow.client.feature.module.impl.world.AutoFish;
import net.shadow.client.feature.module.impl.world.AutoLavacast;
import net.shadow.client.feature.module.impl.world.AutoSign;
import net.shadow.client.feature.module.impl.world.AutoTool;
import net.shadow.client.feature.module.impl.world.BlockTagViewer;
import net.shadow.client.feature.module.impl.world.Boom;
import net.shadow.client.feature.module.impl.world.FastUse;
import net.shadow.client.feature.module.impl.world.Flattener;
import net.shadow.client.feature.module.impl.world.GodBridge;
import net.shadow.client.feature.module.impl.world.Godmode;
import net.shadow.client.feature.module.impl.world.InstantBreak;
import net.shadow.client.feature.module.impl.world.MassUse;
import net.shadow.client.feature.module.impl.world.NewChunks;
import net.shadow.client.feature.module.impl.world.NoBreakDelay;
import net.shadow.client.feature.module.impl.world.Nuker;
import net.shadow.client.feature.module.impl.world.Scaffold;
import net.shadow.client.feature.module.impl.world.SurvivalNuker;
import net.shadow.client.feature.module.impl.world.XRAY;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleRegistry {
    static final List<Module> vanillaModules = new ArrayList<>();
    static final List<AddonModuleEntry> customModules = new ArrayList<>();
    static final List<Module> sharedModuleList = new ArrayList<>();
    static final AtomicBoolean reloadInProgress = new AtomicBoolean(false);
    static final AtomicBoolean initialized = new AtomicBoolean(false);

    public static List<AddonModuleEntry> getCustomModules() {
        return customModules;
    }

    public static void registerAddonModule(Addon source, Module module) {
        for (AddonModuleEntry customModule : customModules) {
            if (customModule.module.getClass() == module.getClass()) {
                throw new IllegalStateException("Module " + module.getClass().getSimpleName() + " already registered");
            }
        }
        customModules.add(new AddonModuleEntry(source, module));
        rebuildSharedModuleList();
    }

    public static void clearCustomModules(Addon addon) {
        for (AddonModuleEntry customModule : customModules) {
            if (customModule.addon == addon && customModule.module.isEnabled()) customModule.module.setEnabled(false);
        }
        customModules.removeIf(addonModuleEntry -> addonModuleEntry.addon == addon);
        rebuildSharedModuleList();
    }

    private static void rebuildSharedModuleList() {
        reloadInProgress.set(true);
        sharedModuleList.clear();
        sharedModuleList.addAll(vanillaModules);
        for (AddonModuleEntry customModule : customModules) {
            sharedModuleList.add(customModule.module);
        }
        reloadInProgress.set(false);
    }

    public static void init() {
        try {
            initInner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerModule(Class<? extends Module> moduleClass) {
        Module instance = null;
        for (Constructor<?> declaredConstructor : moduleClass.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterCount() != 0) {
                throw new IllegalArgumentException(moduleClass.getName() + " has invalid constructor: expected " + moduleClass.getName() + "(), got " + declaredConstructor);
            }
            try {
                instance = (Module) declaredConstructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to make instance of " + moduleClass.getName(), e);
            }
        }
        if (instance == null) {
            throw new IllegalArgumentException("Failed to make instance of " + moduleClass.getName());
        }
        ShadowMain.log(Level.INFO, "Initialized " + instance.getName() + " via " + moduleClass.getName());
        vanillaModules.add(instance);
    }

    private static void initInner() {
        if (initialized.get()) return;
        initialized.set(true);
        vanillaModules.clear();

        registerModule(Flight.class);
        registerModule(Sprint.class);
        registerModule(Fullbright.class);
        registerModule(Hud.class);
        registerModule(TargetHud.class);
        registerModule(AntiOffhandCrash.class);
        registerModule(AntiPacketKick.class);
        registerModule(AntiRDI.class);
        registerModule(BoatPhase.class);
        registerModule(BoatCrash.class);
        registerModule(Boom.class);
        //registerModule(CaveMapper.class);
        registerModule(InstaBow.class);
        registerModule(ChunkCrash.class);
        registerModule(OffhandCrash.class);
        registerModule(OOBCrash.class);
        registerModule(Phase.class);
        registerModule(BrandSpoof.class);
        registerModule(XRAY.class);
        registerModule(Decimator.class);
        registerModule(ClickGUI.class);
        registerModule(TpRange.class);
        registerModule(AnyPlacer.class);
        //registerModule(FireballDeflector.class);
        //registerModule(ShulkerDeflector.class);
        registerModule(CarpetBomb.class);
        registerModule(AutoTrap.class);
        registerModule(AutoTNT.class);
        registerModule(FakeHacker.class);
        registerModule(NoFall.class);
        registerModule(ESP.class);
        registerModule(Tracers.class);
        registerModule(Hyperspeed.class);
        registerModule(AntiAnvil.class);
        registerModule(Swing.class);
        registerModule(AimAssist.class);
        registerModule(Criticals.class);
        registerModule(Killaura.class);
        registerModule(Velocity.class);
        registerModule(AntiAntiXray.class);
        registerModule(PingSpoof.class);
        registerModule(AutoAttack.class);
        //registerModule(MouseEars.class);
        registerModule(Spinner.class);
        registerModule(AllowFormatCodes.class);
        registerModule(InfChatLength.class);
        registerModule(NoTitles.class);
        registerModule(PortalGUI.class);
        registerModule(Timer.class);
        registerModule(XCarry.class);
        registerModule(AirJump.class);
        registerModule(AutoElytra.class);
        registerModule(Blink.class);
        registerModule(Boost.class);
        registerModule(EdgeJump.class);
        registerModule(EdgeSneak.class);
        registerModule(EntityFly.class);
        registerModule(IgnoreWorldBorder.class);
        registerModule(InventoryWalk.class);
        registerModule(Jesus.class);
        registerModule(LongJump.class);
        registerModule(MoonGravity.class);
        registerModule(NoJumpCool.class);
        registerModule(NoLevitation.class);
        registerModule(NoPush.class);
        registerModule(Step.class);
        registerModule(Freecam.class);
        registerModule(FreeLook.class);
        registerModule(ItemByteSize.class);
        registerModule(Zoom.class);
        registerModule(AutoTool.class);
        registerModule(BlockTagViewer.class);
        registerModule(Annihilator.class);
        registerModule(FastUse.class);
        registerModule(Flattener.class);
        registerModule(GodBridge.class);
        registerModule(InstantBreak.class);
        registerModule(MassUse.class);
        registerModule(NoBreakDelay.class);
        registerModule(SurvivalNuker.class);
        registerModule(Nuker.class);
        registerModule(Scaffold.class);
        registerModule(Test.class);
        registerModule(BlocksMCFlight.class);
        registerModule(NameTags.class);
        registerModule(Trail.class);
        registerModule(AdBlock.class);
        registerModule(AutoLavacast.class);
        registerModule(Backtrack.class);
        registerModule(TabGui.class);
        registerModule(Theme.class);
        registerModule(AntiCrash.class);
        registerModule(ClientSettings.class);
        registerModule(NoLiquidFog.class);
        registerModule(Spotlight.class);
        registerModule(ShowTntPrime.class);
        registerModule(ToolsScreen.class);
        registerModule(BookInflaterCrash.class);
        registerModule(BlockHighlighting.class);
        registerModule(AutoIgnite.class);
        registerModule(DiscordRPC.class);
        registerModule(AirPlace.class);
        registerModule(AdSpammer.class);
        registerModule(AnimationCrash.class);
        registerModule(AutoFireball.class);
        registerModule(AutoFish.class);
        registerModule(AutoRun.class);
        registerModule(LecternCrash.class);
        registerModule(MinehutCrash.class);
        registerModule(ArmorStandCrash.class);
        registerModule(LoominaCrash.class);
        registerModule(Reach.class);
        registerModule(Fling.class);
        registerModule(AutoSign.class);
        registerModule(SuperCrossbow.class);
        registerModule(ReverseKnockback.class);
        registerModule(Speed.class);
        registerModule(BoatFling.class);
        registerModule(FilterBypass.class);
        registerModule(InteractCrash.class);
        registerModule(FlightCrash.class);
        registerModule(ClickTP.class);
        registerModule(ChestHighlighter.class);
        registerModule(MoreChatHistory.class);
        registerModule(ClientCrasher.class);
        registerModule(ConsoleSpammer.class);
        registerModule(CraftCrash.class);
        registerModule(ItemPuke.class);
        registerModule(EntityCrash.class);
        registerModule(SSRFCrash.class);
        registerModule(Equipper.class);
        registerModule(Godmode.class);
        registerModule(ErrorCrash.class);
        registerModule(Radar.class);
        registerModule(PaintingCrash.class);
        registerModule(MapFuck.class);
        registerModule(NewChunks.class);
        registerModule(PacketCrash.class);
        registerModule(NoSRP.class);
        registerModule(NoSwing.class);
        registerModule(RequestCrash.class);
        registerModule(NoPacketKick.class);
        registerModule(MoveCrash.class);
        registerModule(MultiShot.class);
        registerModule(Explosion.class);
        registerModule(SwapCrash.class);
        registerModule(ImageLoader.class);
        registerModule(ControlPanel.class);
        registerModule(BeaconSpoofer.class);


        rebuildSharedModuleList();
    }

    public static List<Module> getModules() {
        if (!initialized.get()) {
            init();
        }
        awaitLockOpen();
        return sharedModuleList;
    }

    private static void awaitLockOpen() {
        if (reloadInProgress.get()) {
            ShadowMain.log(Level.INFO, "Locking for some time for reload to complete");
            long lockStart = System.currentTimeMillis();
            long lockStartns = System.nanoTime();
            while (reloadInProgress.get()) {
                Thread.onSpinWait();
            }
            ShadowMain.log(Level.INFO, "Lock opened within " + (System.currentTimeMillis() - lockStart) + " ms (" + (System.nanoTime() - lockStartns) + " ns)");
        }

    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getByClass(Class<T> clazz) {
        if (!initialized.get()) {
            init();
        }
        awaitLockOpen();
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                return (T) module;
            }
        }
        throw new IllegalStateException("Unregistered module: " + clazz.getName());
    }

    public static Module getByName(String n) {
        if (!initialized.get()) {
            init();
        }
        awaitLockOpen();
        for (Module module : getModules()) {
            if (module.getName().equalsIgnoreCase(n)) {
                return module;
            }
        }
        return null;
    }

    public record AddonModuleEntry(Addon addon, Module module) {
    }
}
