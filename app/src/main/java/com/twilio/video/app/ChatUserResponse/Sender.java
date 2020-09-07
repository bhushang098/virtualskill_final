
package com.twilio.video.app.ChatUserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sender {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("profile_path")
    @Expose
    private String profile_path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
