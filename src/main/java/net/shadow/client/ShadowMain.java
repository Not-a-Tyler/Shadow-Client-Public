/*
 * Copyright (c) Shadow client, Saturn5VFive, Breathtaken and contributors 2022. All rights reserved.
 */

package net.shadow.client;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.shadow.client.feature.addon.AddonManager;
import net.shadow.client.feature.command.CommandRegistry;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.gui.FastTickable;
import net.shadow.client.feature.gui.notifications.NotificationRenderer;
import net.shadow.client.feature.itemMenu.ItemGroupRegistry;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.impl.render.ClickGUI;
import net.shadow.client.helper.Rotations;
import net.shadow.client.helper.protection.Locker;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;
import net.shadow.client.helper.font.FontRenderers;
import net.shadow.client.helper.font.adapter.impl.BruhAdapter;
import net.shadow.client.helper.font.renderer.FontRenderer;
import net.shadow.client.helper.manager.ConfigManager;
import net.shadow.client.helper.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ShadowMain implements ModInitializer {

    public static final String MOD_NAME = "Shadow";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final MinecraftClient client = MinecraftClient.getInstance();
    public static final File BASE = new File(MinecraftClient.getInstance().runDirectory, "shadow");
    public static long lastScreenChange = System.currentTimeMillis();
    public static ShadowMain INSTANCE;
    public static Thread MODULE_FTTICKER;
    public static Thread FAST_TICKER;

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        log(Level.INFO, "Initializing");

        if(!isInIde()) Locker.init();

        Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::saveState));
        if (BASE.exists() && !BASE.isDirectory()) {
            BASE.delete();
        }
        if (!BASE.exists()) {
            firstLaunch();
            BASE.mkdir();
        }

        log(Level.INFO, "Loading addons");
        AddonManager.init();

        ConfigManager.loadState();

        ItemGroupRegistry.init();

        log(Level.INFO, "Done initializing");
    }
    void firstLaunch() {
        log(Level.INFO,"First launch, binding ClickGUI to right shift.");
        ModuleRegistry.getByClass(ClickGUI.class).keybind.setValue((double) GLFW.GLFW_KEY_RIGHT_SHIFT);
    }
    boolean isInIde() {
        return true;
    }
    void initFonts() {
        try {
            int fsize = 18 * 2;
            FontRenderers.setRenderer(new BruhAdapter(new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(ShadowMain.class.getClassLoader().getResourceAsStream("Font.ttf"))).deriveFont(Font.PLAIN, fsize), fsize)));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    void tickModulesNWC() {
        for (Module module : ModuleRegistry.getModules()) {
            try {
                if (module.isEnabled()) {
                    module.onFastTick_NWC();
                }
            } catch (Exception ignored) {
            }
        }
    }

    void tickModules() {
        for (Module module : ModuleRegistry.getModules()) {
            try {
                if (module.isEnabled()) {
                    module.onFastTick();
                }
            } catch (Exception ignored) {
            }
        }
    }

    void tickGuiSystem() {
        NotificationRenderer.onFastTick();
        try {
            if (client.currentScreen != null) {
                if (client.currentScreen instanceof FastTickable tickable) {
                    tickable.onFastTick();
                }
                for (Element child : new ArrayList<>(client.currentScreen.children())) { // wow, I hate this
                    if (child instanceof FastTickable t) {
                        t.onFastTick();
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    public void postWindowInit() {
        initFonts();
        ConfigManager.loadState();
        MODULE_FTTICKER = new Thread(() -> {
            while (true) {
                Utils.sleep(10);
                tickModulesNWC(); // always ticks even when we're not in a world
                if (client.player == null || client.world == null) {
                    continue;
                }
                tickModules(); // only ticks when we're in a world
            }
        }, "100_tps_ticker:modules");
        FAST_TICKER = new Thread(() -> {
            while (true) {
                Utils.sleep(10);
                tickGuiSystem(); // ticks gui elements
                //                Themes.tickThemes(); // Tick themes
                if (client.player == null || client.world == null) {
                    continue;
                }
                Rotations.update(); // updates rotations, again only if we are in a world
            }
        }, "100_tps_ticker:gui");
        MODULE_FTTICKER.start();
        FAST_TICKER.start();
        //        ModuleRegistry.sortModulesPostInit();
        CommandRegistry.init();
        log(Level.INFO, "Sending post window init");
        Events.fireEvent(EventType.POST_INIT, new NonCancellableEvent());
        for (Module module : new ArrayList<>(ModuleRegistry.getModules())) {
            module.postInit();
        }
    }

}
