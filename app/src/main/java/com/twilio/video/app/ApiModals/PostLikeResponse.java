package com.twilio.video.app.ApiModals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostLikeResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

}