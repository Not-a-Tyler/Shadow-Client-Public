/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.config.ModuleConfig;
import net.shadow.client.feature.gui.notifications.Notification;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.util.Utils;

import java.lang.annotation.Annotation;

public abstract class Module {

    protected static final MinecraftClient client = ShadowMain.client;
    public final ModuleConfig config;
    public final DoubleSetting keybind;
    private final BooleanSetting debuggerEnabled;
    private final String name;
    private final String description;
    private final ModuleType moduleType;
    private final BooleanSetting toasts;
    private boolean enabled = false;

    public Module(String n, String d, ModuleType type) {
        String first = String.valueOf(d.charAt(0));
        this.name = n;
        this.description = d;
        this.moduleType = type;
        this.config = new ModuleConfig();
        this.keybind = this.config.create(new DoubleSetting.Builder(-1).name("Keybind").description("The keybind to toggle the module with").min(-1).max(65535).precision(0).get());
        //        this.keybind.showIf(() -> false);
        this.debuggerEnabled = this.config.create(new BooleanSetting.Builder(false).name("Debugger").description("Shows a lot of funky visuals describing whats going on").get());
        boolean hasAnnotation = false;
        for (Annotation declaredAnnotation : this.getClass().getDeclaredAnnotations()) {
            if (declaredAnnotation.annotationType() == NoNotificationDefault.class) hasAnnotation = true;
        }
        this.toasts = this.config.create(new BooleanSetting.Builder(!hasAnnotation).name("Toasts").description("Whether to show enabled / disabled toasts").get());
    }

    public boolean isDebuggerEnabled() {
        return this.debuggerEnabled.getValue();
    }

    public final ModuleType getModuleType() {
        return moduleType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void tick();

    public abstract void enable();

    public abstract void disable();

    public abstract String getContext();

    public abstract void onWorldRender(MatrixStack matrices);

    public abstract void onHudRender();

    public void postInit() {

    }

    public void onHudRenderNoMSAA() {

    }

    public void onWorldRenderNoMSAA(MatrixStack matrices) {

    }

    public void onFastTick() {

    }

    public void onFastTick_NWC() {

    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (toasts.getValue()) {
            Notification.create(1000, "Module toggle", Notification.Type.INFO, (this.enabled ? "§aEn" : "§cDis") + "abled §r" + this.getName());
        }
        if (this.enabled) {
            Events.registerEventHandlerClass(this);
            this.enable();
        } else {
            this.disable();
            Events.unregisterEventHandlerClass(this);
        }
    }

}
