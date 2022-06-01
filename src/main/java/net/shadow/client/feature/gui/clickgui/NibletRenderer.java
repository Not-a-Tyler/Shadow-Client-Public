package net.shadow.client.feature.gui.clickgui;

import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.render.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NibletRenderer {
    final List<Niblet> cache = new ArrayList<>();

    public NibletRenderer(int niblets) {
        for (int i = 0; i < niblets; i++) {
            Random random = new Random();
            Niblet niblet = new Niblet(random.nextInt(ShadowMain.client.getWindow().getScaledWidth()), random.nextInt(ShadowMain.client.getWindow().getScaledHeight()), randomEulerRotation(), 0.25F);
            cache.add(niblet);
        }
    }

    public void spawnBreak(double x, double y) {
        Niblet niblet = new Niblet(x, y, randomEulerRotation(), 1);
        cache.add(niblet);
    }

    public void tickPhysics() {
        for (Niblet n : cache) {
            n.x += Math.cos(n.vector) * n.speed;
            n.y += Math.sin(n.vector) * n.speed;
            int flagcode = checkOOB(n);
            if (flagcode > 0) {
                switch (flagcode) {
                    case 1 -> {
                        n.x = ShadowMain.client.getWindow().getScaledWidth();
                        n.vector = Math.toRadians(180 - Math.toDegrees(n.vector));
                    }
                    case 2 -> {
                        n.x = 0;
                        n.vector = Math.toRadians(180 - Math.toDegrees(n.vector));
                    }
                    case 3 -> {
                        n.y = ShadowMain.client.getWindow().getScaledHeight();
                        n.vector = Math.toRadians(360 - Math.toDegrees(n.vector));
                    }
                    case 4 -> {
                        n.y = 0;
                        n.vector = Math.toRadians(360 - Math.toDegrees(n.vector));
                    }
                }
            }
        }
    }


    public void rebder(MatrixStack matrix) {
        for (Niblet n : cache) {
            //RenderUtils.circle2d(n.x + 1, n.y + 1, 2, new Color(125, 125, 125, 150), matrix, 90);
            for (Niblet nr : cache) {
                if (nr == n) continue;
                double distance = distanceto(n.x + 1, n.y + 1, nr.x + 1, nr.y + 1);
                if (distance > 100) continue;
                double lum = (100 - distance) / 100;
                Renderer.R2D.renderLine(matrix, new Color(255, 255, 255, (int) (lum * 255)), n.x + 1, n.y + 1, nr.x + 1, nr.y + 1);
            }
        }
    }

    public int checkOOB(Niblet n) {
        Window window = ShadowMain.client.getWindow();
        if (n.x > window.getScaledWidth()) {
            return 1;
        }
        if (n.x < 0) {
            return 2;
        }
        if (n.y > window.getScaledHeight()) {
            return 3;
        }
        if (n.y < 0) {
            return 4;
        }
        return 0;
    }

    public double distanceto(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static double randomEulerRotation() {
        return (Math.random() - 0.5) * Math.PI * 2;
    }
}
