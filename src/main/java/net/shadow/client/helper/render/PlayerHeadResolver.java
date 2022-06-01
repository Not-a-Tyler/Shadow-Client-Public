/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.render;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.shadow.client.ShadowMain;
import net.shadow.client.helper.Texture;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHeadResolver {
    static final NativeImageBackedTexture EMPTY = new NativeImageBackedTexture(1, 1, false);
    static final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
    static final Map<UUID, NativeImageBackedTexture> imageCache = new HashMap<>();

    public static void resolve(UUID uuid, Texture texture) {
        if (imageCache.containsKey(uuid)) {
            ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(texture, imageCache.get(uuid)));
            return;
        }
        imageCache.put(uuid, EMPTY);
        ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(texture, EMPTY));
        URI u = URI.create("https://mc-heads.net/avatar/" + uuid);
        HttpRequest hr = HttpRequest.newBuilder().uri(u).header("user-agent", "shadow/1.0").build();
        System.out.println("getting " + uuid.toString());
        client.sendAsync(hr, HttpResponse.BodyHandlers.ofByteArray()).thenAccept(httpResponse -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(ImageIO.read(new ByteArrayInputStream(httpResponse.body())), "png", stream);
                byte[] bytes = stream.toByteArray();

                ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
                data.flip();
                NativeImage img = NativeImage.read(data);
                NativeImageBackedTexture nib = new NativeImageBackedTexture(img);

                ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(texture, nib));
                imageCache.put(uuid, nib);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

    }

    public static Texture resolve(UUID uuid) {
        Texture tex = new Texture(String.format("skin_preview-%s", uuid.toString()));
        resolve(uuid, tex);
        return tex;
    }
}
