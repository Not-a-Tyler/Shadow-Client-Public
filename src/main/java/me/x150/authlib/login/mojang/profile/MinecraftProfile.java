package me.x150.authlib.login.mojang.profile;

import java.util.List;
import java.util.UUID;

public class MinecraftProfile {
    private UUID uuid;
    private String username;

    public MinecraftProfile() {
    }

    public MinecraftProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;

    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }



    public String toString() {
        return "MinecraftProfile{uuid=" + this.uuid + ", username='" + this.username + '\'' + '}';
    }
}
