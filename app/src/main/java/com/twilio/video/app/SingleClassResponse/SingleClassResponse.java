
package com.twilio.video.app.SingleClassResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleClassResponse {

    @SerializedName("class")
    @Expose
    private Class _class;
    @SerializedName("wall")
    @Expose
    private Wall wall;
    @SerializedName("user")
    @Expose
    private User user;

    public Class getClass_() {
        return _class;
    }

    public void setClass_(Class _class) {
        this._class = _class;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
