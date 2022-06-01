package net.shadow.client.helper.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.shadow.client.helper.http.HttpWrapper;

import java.io.IOException;
import java.net.http.HttpResponse;

public class DiscordClient {
    final String token;
    final HttpWrapper requests = new HttpWrapper();

    public DiscordClient(String token, boolean isBot) {
        if (isBot) {
            this.token = "Bot " + token;
        } else {
            this.token = token;
        }
    }

    public long[] getGuilds() {
        try {
            HttpResponse<String> resp = requests.get("https://discord.com/api/v8/users/@me/guilds", "Authorization:" + token);
            JsonArray guilds = JsonParser.parseString(resp.body()).getAsJsonArray();
            long[] guildr = new long[guilds.size()];
            int iter = 0;
            for (JsonElement guild : guilds) {
                guildr[iter] = Long.parseLong(guild.getAsJsonObject().get("id").getAsString());
                iter++;
            }
            return guildr;
        } catch (Exception e) {
            return new long[0];
        }
    }

    public long[] getChannels(long guildId) {
        try {
            HttpResponse<String> resp = requests.get("https://discord.com/api/v8/guilds/" + guildId + "/channels", "Authorization:" + token);
            JsonArray guilds = JsonParser.parseString(resp.body()).getAsJsonArray();
            long[] guildr = new long[guilds.size()];
            int iter = 0;
            for (JsonElement guild : guilds) {
                guildr[iter] = Long.parseLong(guild.getAsJsonObject().get("id").getAsString());
                iter++;
            }
            return guildr;
        } catch (Exception e) {
            return new long[0];
        }
    }

    public long[] getDmChannels() {
        try {
            HttpResponse<String> resp = requests.get("https://discord.com/api/v8/users/@me/channels", "Authorization:" + token);
            JsonArray guilds = JsonParser.parseString(resp.body()).getAsJsonArray();
            long[] guildr = new long[guilds.size()];
            int iter = 0;
            for (JsonElement guild : guilds) {
                guildr[iter] = Long.parseLong(guild.getAsJsonObject().get("id").getAsString());
                iter++;
            }
            return guildr;
        } catch (Exception e) {
            return new long[0];
        }
    }

    public long[] getRoles(long guildId) {
        try {
            HttpResponse<String> resp = requests.get("https://discord.com/api/v8/guilds/" + guildId + "/roles", "Authorization:" + token);
            JsonArray guilds = JsonParser.parseString(resp.body()).getAsJsonArray();
            long[] guildr = new long[guilds.size()];
            int iter = 0;
            for (JsonElement guild : guilds) {
                guildr[iter] = Long.parseLong(guild.getAsJsonObject().get("id").getAsString());
                iter++;
            }
            return guildr;
        } catch (Exception e) {
            return new long[0];
        }
    }

    public long[] getMembers(long guildId) {
        try {
            HttpResponse<String> resp = requests.get("https://discord.com/api/v8/guilds/" + guildId + "/members", "Authorization:" + token);
            JsonArray guilds = JsonParser.parseString(resp.body()).getAsJsonArray();
            long[] guildr = new long[guilds.size()];
            int iter = 0;
            for (JsonElement guild : guilds) {
                guildr[iter] = Long.parseLong(guild.getAsJsonObject().get("id").getAsString());
                iter++;
            }
            return guildr;
        } catch (Exception e) {
            return new long[0];
        }
    }


    public long deleteChannel(long channelId) {
        HttpResponse<String> req;
        try {
            req = requests.delete("https://discord.com/api/v9/channels/" + channelId, "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public long createChannel(long guildId, long type, String name) {
        HttpResponse<String> req;
        try {
            req = requests.post("https://discord.com/api/v9/guilds/" + guildId + "/channels", "{\"name\":\"" + name + "\", \"permission_overwrites\":[], \"type\":\"" + type + "\"}", "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public long sendMessage(long channelid, String content, boolean tts) {
        HttpResponse<String> req;
        try {
            req = requests.post("https://discord.com/api/v9/channels/" + channelid + "/messages", "{\"content\":\"" + content + "\", \"tts\":" + tts + "}", "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public long banMember(long guildId, long userId) {
        HttpResponse<String> req;
        try {
            req = requests.put("https://discord.com/api/v9/guilds/" + guildId + "/bans/" + userId + "", "{\"delete_message_days\":0}", "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public long createRole(long guildId, String name) {
        HttpResponse<String> req;
        try {
            req = requests.post("https://discord.com/api/v9/guilds/" + guildId + "/roles", "{\"name\":\"" + name + "\"}", "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public long deleteRole(long guildId, long roleId) {
        try {
            HttpResponse<String> req;
            req = requests.delete("https://discord.com/api/v9/guilds/" + roleId + "/roles", "Authorization:" + token);
            return req.statusCode();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

}
