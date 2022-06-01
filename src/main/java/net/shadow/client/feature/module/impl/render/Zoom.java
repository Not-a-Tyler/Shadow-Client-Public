/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.shadow.client.feature.config.BooleanSetting;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleRegistry;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.Keybind;
import net.shadow.client.helper.render.Renderer;

import java.util.Objects;

public class Zoom extends Module {

    static long enabledTime = 0;
    final DoubleSetting finalFov = this.config.create(new DoubleSetting.Builder(30).name("FOV").description("How far to zoom in").min(1).max(180).precision(0).get());
    final BooleanSetting hold = this.config.create(new BooleanSetting.Builder(true).name("Hold").description("Disables the module when you unpress the keybind").get());

    Keybind kb;
    double msens = 0.5d;

    public Zoom() {
        super("Zoom", "Imitates the spyglass with more options", ModuleType.RENDER);
        //        finalFov = (SliderValue) this.config.create("Fov", 30, 1, 180, 0).description("The FOV to zoom into");
    }

    static double easeOutBounce(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    public double getZoomValue(double vanilla) {
        long enabledFor = System.currentTimeMillis() - enabledTime;
        double prog = MathHelper.clamp(enabledFor / 100d, 0, 1);
        if (!Objects.requireNonNull(ModuleRegistry.getByClass(Zoom.class)).isEnabled()) {
            prog = Math.abs(1 - prog);
        }
        prog = easeOutBounce(prog);
        return Renderer.Util.lerp(vanilla, finalFov.getValue(), prog);
    }

    @Override
    public void tick() {
        if (kb == null) {
            return;
        }
        if (!kb.isPressed() && hold.getValue()) {
            this.setEnabled(false);
        }
    }

    @Override
    public void enable() {
        msens = client.options.mouseSensitivity;
        client.options.mouseSensitivity = msens * (finalFov.getValue() / client.options.fov);
        // retard the keybind thing is always an int shut the fuck up
        kb = new Keybind((int) (keybind.getValue() + 0));
        enabledTime = System.currentTimeMillis();
    }

    @Override
    public void disable() {
        enabledTime = System.currentTimeMillis();
        client.options.mouseSensitivity = msens;
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

