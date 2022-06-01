/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.gui.hud;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.gui.hud.element.HudElement;
import net.shadow.client.feature.gui.hud.element.RadarElement;
import net.shadow.client.feature.gui.hud.element.SpeedHud;
import net.shadow.client.feature.gui.hud.element.TabGui;
import net.shadow.client.feature.gui.hud.element.Taco;
import net.shadow.client.feature.gui.hud.element.TargetHUD;
import net.shadow.client.feature.gui.screen.HudEditorScreen;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.MouseEvent;
import net.shadow.client.helper.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HudRenderer {

    static final File CONFIG = new File(ShadowMain.BASE, "hud.shadow");
    private static HudRenderer INSTANCE;
    final List<HudElement> elements = register();
    boolean isEditing = false;
    boolean mouseHeldDown = false;
    double prevX = Utils.Mouse.getMouseX();
    double prevY = Utils.Mouse.getMouseY();
    double prevWX = ShadowMain.client.getWindow().getScaledWidth();
    double prevWY = ShadowMain.client.getWindow().getScaledHeight();

    private HudRenderer() {
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!isEditing) {
                return;
            }
            MouseEvent me = (MouseEvent) event;
            if (me.getAction() == 1) {
                mouseHeldDown = true;
                prevX = Utils.Mouse.getMouseX();
                prevY = Utils.Mouse.getMouseY();
                for (HudElement element : elements) {
                    if (element.mouseClicked(Utils.Mouse.getMouseX(), Utils.Mouse.getMouseY())) {
                        break;
                    }
                }
            } else if (me.getAction() == 0) {
                mouseHeldDown = false;
                for (HudElement element : elements) {
                    element.mouseReleased();
                }
            }
        });
        Events.registerEventHandler(EventType.CONFIG_SAVE, event -> saveConfig());
        loadConfig();
    }

    public static HudRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HudRenderer();
        }
        return INSTANCE;
    }

    static List<HudElement> register() {
        List<HudElement> he = new ArrayList<>();
        he.add(new TargetHUD());
        he.add(new Taco());
        he.add(new SpeedHud());
        he.add(new TabGui());
        he.add(new RadarElement());
        return he;
    }

    void saveConfig() {
        ShadowMain.log(Level.INFO, "Saving hud");
        JsonArray root = new JsonArray();
        for (HudElement element : elements) {
            JsonObject current = new JsonObject();
            current.addProperty("id", element.getId());
            current.addProperty("px", element.getPosX());
            current.addProperty("py", element.getPosY());
            root.add(current);
        }
        try {
            FileUtils.write(CONFIG, root.toString(), StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            ShadowMain.log(Level.ERROR, "Failed to write hud file");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    void loadConfig() {
        ShadowMain.log(Level.INFO, "Loading hud");
        if (!CONFIG.isFile()) {
            CONFIG.delete();
        }
        if (!CONFIG.exists()) {
            ShadowMain.log(Level.INFO, "Skipping hud loading because file doesn't exist");
            return;
        }
        try {
            String contents = FileUtils.readFileToString(CONFIG, StandardCharsets.UTF_8);
            JsonArray ja = JsonParser.parseString(contents).getAsJsonArray();
            for (JsonElement jsonElement : ja) {
                JsonObject jo = jsonElement.getAsJsonObject();
                String id = jo.get("id").getAsString();
                int x = jo.get("px").getAsInt();
                int y = jo.get("py").getAsInt();
                for (HudElement element : elements) {
                    if (element.getId().equalsIgnoreCase(id)) {
                        element.setPosX(x);
                        element.setPosY(y);
                    }
                }
            }
        } catch (Exception ignored) {
            ShadowMain.log(Level.ERROR, "Failed to read hud file - corrupted?");
        }
        ShadowMain.log(Level.INFO, "Loaded hud");
    }

    public void fastTick() {
        double currentWX = ShadowMain.client.getWindow().getScaledWidth();
        double currentWY = ShadowMain.client.getWindow().getScaledHeight();
        if (currentWX != prevWX) {
            for (HudElement element : elements) {
                double px = element.getPosX();
                double perX = px / prevWX;
                element.setPosX(currentWX * perX);
            }
            prevWX = currentWX;
        }
        if (currentWY != prevWY) {
            for (HudElement element : elements) {
                double py = element.getPosY();
                double perY = py / prevWY;
                element.setPosY(currentWY * perY);
            }
            prevWY = currentWY;
        }
        isEditing = ShadowMain.client.currentScreen instanceof HudEditorScreen;
        if (mouseHeldDown) {
            for (HudElement element : elements) {
                element.mouseDragged(Utils.Mouse.getMouseX() - prevX, Utils.Mouse.getMouseY() - prevY);
            }
            prevX = Utils.Mouse.getMouseX();
            prevY = Utils.Mouse.getMouseY();
        }
        for (HudElement element : elements) {
            element.fastTick();
        }
    }

    public void render() {
        for (HudElement element : elements) {
            element.render();
            if (isEditing) {
                element.renderOutline();
            }
        }
    }
}
