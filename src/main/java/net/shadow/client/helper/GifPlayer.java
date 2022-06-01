/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.helper.font.FontRenderers;
import net.shadow.client.helper.render.Renderer;
import net.shadow.client.helper.util.Utils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GifPlayer {
    final int fps;
    final List<Frame> frameList = new ArrayList<>();
    Object gifFile;
    long currentFrame = 0;
    long lastRender = System.currentTimeMillis();

    private GifPlayer(Object file, int fps) {
        this.gifFile = file;
        this.fps = fps;
        //        globalInstances.add(this);
        init();

    }

    public static GifPlayer createFromFile(File file, int fps) {
        return new GifPlayer(file, fps);
    }

    public static GifPlayer createFromInputStream(InputStream is, int fps) {
        return new GifPlayer(is, fps);
    }

    public void setGifFile(File gifFile) {
        this.gifFile = gifFile;
        init();
    }

    void init() {
        for (Frame frame : frameList) {
            frame.clear();
        }
        frameList.clear();
        try {
            String[] imageatt = new String[] { "imageLeftPosition", "imageTopPosition", "imageWidth", "imageHeight" };

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream ciis = ImageIO.createImageInputStream(gifFile);
            reader.setInput(ciis, false);

            int noi = reader.getNumImages(true);
            BufferedImage master = null;

            for (int i = 0; i < noi; i++) {
                BufferedImage image = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        Map<String, Integer> imageAttr = new HashMap<>();

                        for (String s : imageatt) {
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(s);
                            imageAttr.put(s, Integer.valueOf(attnode.getNodeValue()));
                        }
                        if (master == null) {
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }
                        master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                    }
                }
                Frame f = new Frame("lmfao", master);
                frameList.add(f);
                //                ImageIO.write(master, "GIF", new File( i + ".gif"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentFrame = 0;
    }

    public void progressFrame() {
        currentFrame++;
        currentFrame %= frameList.size();
    }

    public Frame getCurrentFrame() {
        return frameList.get((int) currentFrame);
    }

    public void renderFrame(MatrixStack stack, double x, double y, double width, double height) {
        long timeDelta = System.currentTimeMillis() - lastRender;
        long eachXmsFrame = 1000 / fps;
        int framesToProgress = (int) (timeDelta / eachXmsFrame);
        if (framesToProgress > 0) lastRender = System.currentTimeMillis();
        for (int i = 0; i < framesToProgress; i++) {
            progressFrame();
        }
        Frame current = getCurrentFrame();
        RenderSystem.setShaderTexture(0, current.getTexture());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Renderer.R2D.renderTexture(stack, x, y, width, height, 0, 0, width, height, width, height);
        FontRenderers.getRenderer().drawString(stack, currentFrame + "/" + frameList.size(), x, y, 0);
    }

    static class Frame {
        final Texture texture;

        public Frame(String id, BufferedImage bi) {
            String rndId = Integer.toHexString((int) Math.floor(Math.random() * 0xFFFFFF));
            this.texture = new Texture("gifPlayer/" + id + "-" + rndId);
            Utils.registerBufferedImageTexture(texture, bi);
        }

        public void clear() {
            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().destroyTexture(texture));
        }

        public Texture getTexture() {
            return texture;
        }
    }
}
