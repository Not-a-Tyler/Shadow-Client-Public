package me.x150.authlib.login.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import me.x150.authlib.exception.AuthFailureException;
import me.x150.authlib.login.altening.AlteningAuth;
import me.x150.authlib.login.microsoft.MicrosoftAuthenticator;
import me.x150.authlib.login.microsoft.XboxToken;
import me.x150.authlib.login.mojang.profile.MinecraftProfile;
import me.x150.authlib.struct.Authenticator;
public class MinecraftAuthenticator extends Authenticator<MinecraftToken> {
    protected final MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();

    public MinecraftAuthenticator() {
    }

    public MinecraftToken login(String email, String password) {
        try {
            URL url = new URL("https://authserver.mojang.com/authenticate");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            JsonObject request = new JsonObject();
            JsonObject agent = new JsonObject();
            agent.addProperty("name", "Minecraft");
            agent.addProperty("version", "1");
            request.add("agent", agent);
            request.addProperty("username", email);
            request.addProperty("password", password);
            request.addProperty("requestUser", false);
            String requestBody = request.toString();

            httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            // httpURLConnection.setRequestProperty("Host", "authserver.mojang.com");
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();

            try {
                outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
            } catch (Throwable var13) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }

                throw var13;
            }

            if (outputStream != null) {
                outputStream.close();
            }
            JsonObject jsonObject = this.parseResponseData(httpURLConnection);

            return new MinecraftToken(jsonObject.get("accessToken").getAsString(), jsonObject.get("selectedProfile").getAsJsonObject().get("name").getAsString(),generateUUID(jsonObject.get("selectedProfile").getAsJsonObject().get("id").getAsString()));
        } catch (IOException var14) {
            throw new AuthFailureException(String.format("Authentication error. Request could not be made! Cause: '%s'", var14.getMessage()));
        }
    }

    public MinecraftToken loginWithMicrosoft(String email, String password) {
        XboxToken xboxToken = this.microsoftAuthenticator.login(email, password);

        try {
            URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            JsonObject request = new JsonObject();
            request.addProperty("identityToken", "XBL3.0 x=" + xboxToken.getUhs() + ";" + xboxToken.getToken());
            String requestBody = request.toString();
            httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();

            try {
                outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
            } catch (Throwable var13) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }

                throw var13;
            }

            if (outputStream != null) {
                outputStream.close();
            }

            JsonObject jsonObject = this.microsoftAuthenticator.parseResponseData(httpURLConnection);
            return new MinecraftToken(jsonObject.get("access_token").getAsString(), jsonObject.get("username").getAsString(),UUID.fromString(jsonObject.get("selected_profile").getAsJsonObject().get("id").getAsString()));
        } catch (IOException var14) {
            throw new AuthFailureException(String.format("Authentication error. Request could not be made! Cause: '%s'", var14.getMessage()));
        }
    }
    public MinecraftToken loginWithAltening(String token) {
        AlteningAuth alteningAuth = new AlteningAuth(token);
        return alteningAuth.login();
    }

    public MinecraftProfile getGameProfile(MinecraftToken minecraftToken) {
        try {
            if(isForceMigrated(minecraftToken)) {
                // this request is completly useless....
               return new MinecraftProfile(minecraftToken.getUuid(),minecraftToken.getUsername());
            }
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization",  "Bearer "+minecraftToken.getAccessToken());
            httpURLConnection.connect();
            JsonObject jsonObject = this.parseResponseData(httpURLConnection);

            UUID uuid = this.generateUUID(jsonObject.get("id").getAsString());
            String name = jsonObject.get("name").getAsString();


            return new MinecraftProfile(uuid, name);
        } catch (IOException var10) {
            throw new AuthFailureException(String.format("Authentication error. Request could not be made! Cause: '%s'", var10.getMessage()));
        }
    }
    public boolean isForceMigrated(MinecraftToken minecraftToken) {
        try {

            URL url = new URL("https://api.minecraftservices.com/rollout/v1/msamigrationforced");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization",  "Bearer "+minecraftToken.getAccessToken());
            httpURLConnection.connect();
            JsonObject jsonObject = this.parseResponseData(httpURLConnection);
            boolean rollout = jsonObject.get("rollout").getAsBoolean();


            return rollout;
        } catch (IOException var10) {
            throw new AuthFailureException(String.format("Authentication error. Request could not be made! Cause: '%s'", var10.getMessage()));
        }
    }
    public JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException {

        InputStream stream = httpURLConnection.getInputStream();

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (stream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return this.gson.fromJson(textBuilder.toString(),JsonObject.class);
    }

    public UUID generateUUID(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) {
            throw new IllegalArgumentException();
        } else {
            StringBuilder builder = new StringBuilder(trimmedUUID.trim());

            try {
                builder.insert(20, "-");
                builder.insert(16, "-");
                builder.insert(12, "-");
                builder.insert(8, "-");
                return UUID.fromString(builder.toString());
            } catch (StringIndexOutOfBoundsException var4) {
                return null;
            }
        }
    }
}
