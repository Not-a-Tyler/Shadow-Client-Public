/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package net.shadow.client.helper.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.gui.screen.ConsoleScreen;
import net.shadow.client.helper.Texture;
import net.shadow.client.helper.font.adapter.FontAdapter;
import net.shadow.client.mixin.IMinecraftClientAccessor;
import net.shadow.client.mixin.IRenderTickCounterAccessor;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {

    public static boolean sendPackets = true;

    public static String rndStr(int size) {
        StringBuilder buf = new StringBuilder();
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            buf.append(chars[r.nextInt(chars.length)]);
        }
        return buf.toString();
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    public static void sendDiscordFile(String url, String msgContent, String filename, byte[] file) throws IOException, InterruptedException {
        long e = System.currentTimeMillis();
        byte[] body1 = ("----" + e + "\n" + "Content-Disposition: form-data; name=\"file\"; filename=\"" + filename + "\"\n" + "Content-Type: text/plain\n\n").getBytes(StandardCharsets.UTF_8);
        byte[] body2 = ("\n" + "----" + e + "\n" + "Content-Disposition: form-data; name=\"payload_json\"\n" + "\n" + "{\"content\":\"" + msgContent + "\",\"tts\":false}\n" + "----" + e + "--\n").getBytes(StandardCharsets.UTF_8);
        byte[] finalBody = new byte[body1.length + file.length + body2.length];
        System.arraycopy(body1, 0, finalBody, 0, body1.length);
        System.arraycopy(file, 0, finalBody, body1.length, file.length);
        System.arraycopy(body2, 0, finalBody, body1.length + file.length, body2.length);
        HttpClient real = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofByteArray(finalBody)).uri(URI.create(url)).header("User-Agent", "your mother").header("Content-Type", "multipart/form-data; boundary=--" + e).build();
        System.out.println(real.send(req, HttpResponse.BodyHandlers.ofString()).body());
    }


    public static void sendPacket(Packet<?> packet) {
        sendPackets = false;
        ShadowMain.client.player.networkHandler.sendPacket(packet);
        sendPackets = true;
    }

    public static void setClientTps(float tps) {
        IRenderTickCounterAccessor accessor = ((IRenderTickCounterAccessor) ((IMinecraftClientAccessor) ShadowMain.client).getRenderTickCounter());
        accessor.setTickTime(1000f / tps);
    }

    public static Color getCurrentRGB() {
        return new Color(Color.HSBtoRGB((System.currentTimeMillis() % 4750) / 4750f, 0.5f, 1));
    }

    public static Vec3d getInterpolatedEntityPosition(Entity entity) {
        Vec3d a = entity.getPos();
        Vec3d b = new Vec3d(entity.prevX, entity.prevY, entity.prevZ);
        float p = ShadowMain.client.getTickDelta();
        return new Vec3d(MathHelper.lerp(p, b.x, a.x), MathHelper.lerp(p, b.y, a.y), MathHelper.lerp(p, b.z, a.z));
    }

    public static void registerBufferedImageTexture(Texture i, BufferedImage bi) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            byte[] bytes = baos.toByteArray();

            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
            data.flip();
            NativeImageBackedTexture tex = new NativeImageBackedTexture(NativeImage.read(data));
            ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(i, tex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] splitLinesToWidth(String input, double maxWidth, FontAdapter rendererUsed) {
        List<String> dSplit = List.of(input.split("\n"));
        List<String> splits = new ArrayList<>();
        for (String s : dSplit) {
            List<String> splitContent = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            for (String c : s.split(" ")) {
                if (rendererUsed.getStringWidth(line + c) >= maxWidth - 10) {
                    splitContent.add(line.toString().trim());
                    line = new StringBuilder();
                }
                line.append(c).append(" ");
            }
            splitContent.add(line.toString().trim());
            splits.addAll(splitContent);
        }
        return splits.toArray(new String[0]);
    }

    public static ItemStack generateItemStackWithMeta(String nbt, Item item) {
        try {
            ItemStack stack = new ItemStack(item);
            stack.setNbt(StringNbtReader.parse(nbt));
            return stack;
        } catch (Exception ignored) {
            return new ItemStack(item);
        }
    }

    public static class Textures {
        static final NativeImageBackedTexture EMPTY = new NativeImageBackedTexture(1, 1, false);
        static final HttpClient downloader = HttpClient.newHttpClient();
        static final Map<UUID, Texture> uCache = new ConcurrentHashMap<>();

        public static Texture getSkinPreviewTexture(UUID uuid) {
            if (uCache.containsKey(uuid)) {
                return uCache.get(uuid);
            }
            // completely random id
            // (hash code of uuid)(random numbers)
            Texture texIdent = new Texture("preview/" + uuid.hashCode() + "");
            uCache.put(uuid, texIdent);
            HttpRequest hr = HttpRequest.newBuilder().uri(URI.create("https://crafatar.com/avatars/" + uuid + "?overlay")).header("User-Agent", "why").build();
            ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(texIdent, EMPTY));
            downloader.sendAsync(hr, HttpResponse.BodyHandlers.ofByteArray()).thenAccept(httpResponse -> {
                try {
                    BufferedImage bi = ImageIO.read(new ByteArrayInputStream(httpResponse.body()));
                    registerBufferedImageTexture(bi, texIdent);
                    uCache.put(uuid, texIdent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return texIdent;
        }

        public static void registerBufferedImageTexture(BufferedImage image, Texture tex) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", stream);
                byte[] bytes = stream.toByteArray();

                ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
                data.flip();
                NativeImage img = NativeImage.read(data);
                NativeImageBackedTexture texture = new NativeImageBackedTexture(img);

                ShadowMain.client.execute(() -> ShadowMain.client.getTextureManager().registerTexture(tex, texture));
            } catch (Exception ignored) {

            }
        }
    }

    public static class Inventory {

        public static int slotIndexToId(int index) {
            int translatedSlotId;
            if (index >= 0 && index < 9) {
                translatedSlotId = 36 + index;
            } else {
                translatedSlotId = index;
            }
            return translatedSlotId;
        }

        public static void drop(int index) {
            int translatedSlotId = slotIndexToId(index);
            Objects.requireNonNull(ShadowMain.client.interactionManager).clickSlot(Objects.requireNonNull(ShadowMain.client.player).currentScreenHandler.syncId, translatedSlotId, 1, SlotActionType.THROW, ShadowMain.client.player);
        }

        public static void moveStackToOther(int slotIdFrom, int slotIdTo) {
            Objects.requireNonNull(ShadowMain.client.interactionManager).clickSlot(0, slotIdFrom, 0, SlotActionType.PICKUP, ShadowMain.client.player); // pick up item from stack
            ShadowMain.client.interactionManager.clickSlot(0, slotIdTo, 0, SlotActionType.PICKUP, ShadowMain.client.player); // put item to target
            ShadowMain.client.interactionManager.clickSlot(0, slotIdFrom, 0, SlotActionType.PICKUP, ShadowMain.client.player); // (in case target slot had item) put item from target back to from
        }
    }

    public static class Math {

        public static boolean isNumber(String in) {
            try {
                Integer.parseInt(in);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }

        public static double roundToDecimal(double n, int point) {
            if (point == 0) {
                return java.lang.Math.floor(n);
            }
            double factor = java.lang.Math.pow(10, point);
            return java.lang.Math.round(n * factor) / factor;
        }

        public static int tryParseInt(String input, int defaultValue) {
            try {
                return Integer.parseInt(input);
            } catch (Exception ignored) {
                return defaultValue;
            }
        }

        public static Vec3d getRotationVector(float pitch, float yaw) {
            float f = pitch * 0.017453292F;
            float g = -yaw * 0.017453292F;
            float h = MathHelper.cos(g);
            float i = MathHelper.sin(g);
            float j = MathHelper.cos(f);
            float k = MathHelper.sin(f);
            return new Vec3d(i * j, -k, h * j);
        }

        public static boolean isABObstructed(Vec3d a, Vec3d b, World world, Entity requester) {
            RaycastContext rcc = new RaycastContext(a, b, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, requester);
            BlockHitResult bhr = world.raycast(rcc);
            return !bhr.getPos().equals(b);
        }
    }
    
    public static Vec3d relativeToAbsolute(Vec3d absRootPos, Vec2f rotation, Vec3d relative) {
        double xOffset = relative.x;
        double yOffset = relative.y;
        double zOffset = relative.z;
        float rot = 0.017453292F;
        float f = MathHelper.cos((rotation.y + 90.0F) * rot);
        float g = MathHelper.sin((rotation.y + 90.0F) * rot);
        float h = MathHelper.cos(-rotation.x * rot);
        float i = MathHelper.sin(-rotation.x * rot);
        float j = MathHelper.cos((-rotation.x + 90.0F) * rot);
        float k = MathHelper.sin((-rotation.x + 90.0F) * rot);
        Vec3d vec3d2 = new Vec3d(f * h, i, g * h);
        Vec3d vec3d3 = new Vec3d(f * j, k, g * j);
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0D);
        double d = vec3d2.x * zOffset + vec3d3.x * yOffset + vec3d4.x * xOffset;
        double e = vec3d2.y * zOffset + vec3d3.y * yOffset + vec3d4.y * xOffset;
        double l = vec3d2.z * zOffset + vec3d3.z * yOffset + vec3d4.z * xOffset;
        return new Vec3d(absRootPos.x + d, absRootPos.y + e, absRootPos.z + l);
    }

    public static class Mouse {

        public static double getMouseX() {
            return ShadowMain.client.mouse.getX() / ShadowMain.client.getWindow().getScaleFactor();
        }

        public static double getMouseY() {
            return ShadowMain.client.mouse.getY() / ShadowMain.client.getWindow().getScaleFactor();
        }
    }

    //---DO NOT REMOVE THIS---
    public static class Packets {

        public static PlayerInteractBlockC2SPacket generatePlace(BlockPos pos) {
            return new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Direction.UP, pos, false));
        }

    }

    public static class Players {

        static final Map<String, UUID> UUID_CACHE = new HashMap<>();
        static final Map<UUID, String> NAME_CACHE = new HashMap<>();
        static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

        public static String getNameFromUUID(UUID uuid) {
            if (NAME_CACHE.containsKey(uuid)) {
                return NAME_CACHE.get(uuid);
            }
            try {
                HttpRequest req = HttpRequest.newBuilder().GET().uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid)).build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 204 || response.statusCode() == 400) {
                    return null; // no user / invalid username
                }
                JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
                return root.get("name").getAsString();
            } catch (Exception ignored) {
                return null;
            }
        }

        public static String completeName(String smallname) {
            String result = "none";
            for (PlayerListEntry info : ShadowMain.client.player.networkHandler.getPlayerList()) {
                String name = info.getProfile().getName();

                if (name.toLowerCase().startsWith(smallname.toLowerCase())) {
                    result = name;
                }
            }
            if (result.equals("none")) return smallname;
            return result;
        }

        public static UUID getUUIDFromName(String name) {
            String name1 = completeName(name); // this really helps trust me
            if (!isPlayerNameValid(name1)) {
                return null;
            }
            if (UUID_CACHE.containsKey(name1.toLowerCase())) {
                return UUID_CACHE.get(name1.toLowerCase());
            }
            try {
                HttpRequest req = HttpRequest.newBuilder().GET().uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name1)).build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 204 || response.statusCode() == 400) {
                    return null; // no user / invalid username
                }
                JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
                String id = root.get("id").getAsString();
                String uuid = id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                UUID u = UUID.fromString(uuid);
                UUID_CACHE.put(name1.toLowerCase(), u);
                return u;
            } catch (Exception ignored) {
                return null;
            }
        }

        public static boolean isPlayerNameValid(String name) {
            if (name.length() < 3 || name.length() > 16) {
                return false;
            }
            String valid = "abcdefghijklmnopqrstuvwxyz0123456789_";
            boolean isValidEntityName = true;
            for (char c : name.toLowerCase().toCharArray()) {
                if (!valid.contains(c + "")) {
                    isValidEntityName = false;
                    break;
                }
            }
            return isValidEntityName;
        }

        public static int[] decodeUUID(UUID uuid) {
            long sigLeast = uuid.getLeastSignificantBits();
            long sigMost = uuid.getMostSignificantBits();
            return new int[] { (int) (sigMost >> 32), (int) sigMost, (int) (sigLeast >> 32), (int) sigLeast };
        }
    }

    public static class Logging {
        static final Queue<Text> messageQueue = new ArrayDeque<>();

        static void sendMessages() {
            if (ShadowMain.client.player != null) {
                Text next;
                while ((next = messageQueue.poll()) != null) {
                    ShadowMain.client.player.sendMessage(next, false);
                }
            }
        }

        public static void warn(String n) {
            message(n, Color.YELLOW);
        }

        public static void success(String n) {
            message(n, new Color(65, 217, 101));
        }

        public static void error(String n) {
            message(n, new Color(214, 93, 62));
        }

        public static void message(String n) {
            message(n, Color.WHITE);
        }

        public static void message(Text text) {
            messageQueue.add(text);
        }

        public static void message(String n, Color c) {
            LiteralText t = new LiteralText(n);
            t.setStyle(t.getStyle().withColor(TextColor.fromRgb(c.getRGB())));
            if (!(ShadowMain.client.currentScreen instanceof ConsoleScreen)) message(t);
            //            if (c.equals(Color.WHITE)) c = Color.BLACK;
            ConsoleScreen.instance().addLog(new ConsoleScreen.LogEntry(n, c));
        }

    }

    public static class TickManager {

        static final List<TickEntry> entries = new ArrayList<>();
        static final List<Runnable> nextTickRunners = new ArrayList<>();

        public static void runInNTicks(int n, Runnable toRun) {
            entries.add(new TickEntry(n, toRun));
        }

        public static void tick() {
            Logging.sendMessages();
            for (TickEntry entry : entries.toArray(new TickEntry[0])) {
                entry.v--;
                if (entry.v <= 0) {
                    entry.r.run();
                    entries.remove(entry);
                }
            }
        }

        public static void runOnNextRender(Runnable r) {
            if (ShadowMain.client.options.hudHidden) return;
            nextTickRunners.add(r);
        }

        public static void render() {
            for (Runnable nextTickRunner : nextTickRunners) {
                nextTickRunner.run();
            }
            nextTickRunners.clear();
        }

        static class TickEntry {

            final Runnable r;
            int v;

            public TickEntry(int v, Runnable r) {
                this.v = v;
                this.r = r;
            }
        }
    }
}
