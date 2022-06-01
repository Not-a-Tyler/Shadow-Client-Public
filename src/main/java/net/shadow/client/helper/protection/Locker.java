package net.shadow.client.helper.protection;

import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.IPCUser;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.MinecraftClient;

public class Locker {
    private static List<String> IDS = Arrays.asList("661988735189254165","956655656704815124");
    public static void init() {
        boolean result = DiscordIPC.start(958479347390500874L, () -> {
            IPCUser user = DiscordIPC.getUser();
            if(!IDS.contains(user.id)) MinecraftClient.getInstance().close();

            DiscordIPC.stop();
        });
        if (!result) {
            System.out.println("Please have discord open while launching the client");
            Runtime.getRuntime().exit(1);
        }
    }

}
