
package com.twilio.video.app.HomePostModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomePostModal {

    @SerializedName("posts")
    @Expose
    private Posts posts;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

}
