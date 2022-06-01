

package me.x150.authlib.login.microsoft;

import me.x150.authlib.struct.AuthToken;

public class XboxLiveToken extends AuthToken {
    protected String token;
    protected String uhs;

    public XboxLiveToken() {
    }

    public XboxLiveToken(String token, String uhs) {
        this.token = token;
        this.uhs = uhs;
    }

    public String getToken() {
        return this.token;
    }

    public String getUhs() {
        return this.uhs;
    }
}
