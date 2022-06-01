package me.x150.authlib.login.mojang;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {
    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }
    public class SelectedProfile {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("clientToken")
    @Expose
    private String clientToken;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("selectedProfile")
    @Expose
    private SelectedProfile selectedProfile;
    @SerializedName("availableProfiles")
    @Expose
    private List<AvailableProfile> availableProfiles = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public SelectedProfile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(SelectedProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public List<AvailableProfile> getAvailableProfiles() {
        return availableProfiles;
    }

    public void setAvailableProfiles(List<AvailableProfile> availableProfiles) {
        this.availableProfiles = availableProfiles;
    }
    public class AvailableProfile {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("id")
        @Expose
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

}
