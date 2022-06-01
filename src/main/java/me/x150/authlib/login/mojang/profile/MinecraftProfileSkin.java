package me.x150.authlib.login.mojang.profile;

import me.x150.authlib.struct.OnlineTexture;

public class MinecraftProfileSkin extends OnlineTexture {
    private String variant;

    public MinecraftProfileSkin() {
    }

    public MinecraftProfileSkin(String variant) {
        this.variant = variant;
    }

    public MinecraftProfileSkin(String id, String state, String url, String alias, String variant) {
        super(id, state, url, alias);
        this.variant = variant;
    }

    public String getVariant() {
        return this.variant;
    }

    public String toString() {
        return "MinecraftSkin{id='" + this.getId() + '\'' + ", state='" + this.getState() + '\'' + ", url='" + this.getUrl() + '\'' + ", alias='" + this.getAlias() + '\'' + "variant='" + this.variant + '\'' + '}';
    }
}
