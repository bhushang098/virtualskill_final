
package com.twilio.video.app.SettingsResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("current_password")
    @Expose
    private List<String> currentPassword = null;
    @SerializedName("password")
    @Expose
    private List<String> password = null;

    public List<String> getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(List<String> currentPassword) {
        this.currentPassword = currentPassword;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

}
