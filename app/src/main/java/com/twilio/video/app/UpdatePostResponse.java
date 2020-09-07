
package com.twilio.video.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePostResponse {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("token")
    @Expose
    private String token;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
