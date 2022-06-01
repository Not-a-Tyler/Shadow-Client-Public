package me.x150.authlib.struct;

public abstract class OnlineTexture {
    private String id;
    private String state;
    private String url;
    private String alias;

    public OnlineTexture() {
    }

    public OnlineTexture(String id, String state, String url, String alias) {
        this.id = id;
        this.state = state;
        this.url = url;
        this.alias = alias;
    }

    public String getId() {
        return this.id;
    }

    public String getState() {
        return this.state;
    }

    public String getUrl() {
        return this.url;
    }

    public String getAlias() {
        return this.alias;
    }

    public String toString() {
        return "TextureVariable{id='" + this.id + '\'' + ", state='" + this.state + '\'' + ", url='" + this.url + '\'' + ", alias='" + this.alias + '\'' + '}';
    }
}