

package me.x150.authlib.login.microsoft;

import me.x150.authlib.struct.AuthToken;

public class MicrosoftToken extends AuthToken {
    protected String token;
    protected String refreshToken;

    public MicrosoftToken() {
    }

    public MicrosoftToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return this.token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
