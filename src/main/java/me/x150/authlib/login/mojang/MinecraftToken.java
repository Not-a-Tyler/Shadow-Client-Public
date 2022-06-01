package me.x150.authlib.login.mojang;

import java.util.UUID;

public class MinecraftToken {
    private String accessToken;
    private String username;

    private UUID uuid;


    public MinecraftToken() {
    }

    public MinecraftToken(String accessToken, String username, UUID uuid) {
        this.accessToken = accessToken;
        this.username = username;

        this.uuid = uuid;
    }



    public UUID getUuid() {
        return uuid;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getUsername() {
        return this.username;
    }


    public String toString() {
        return "MinecraftToken{accessToken='" + this.accessToken + '\'' + ", username='" + this.username + '\'' + '}';
    }
}
