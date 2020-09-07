
package com.twilio.video.app.SingelUSerByIDResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Following {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("following_user_id")
    @Expose
    private Integer followingUserId;
    @SerializedName("follower_user_id")
    @Expose
    private Integer followerUserId;
    @SerializedName("allow")
    @Expose
    private Integer allow;
    @SerializedName("following")
    @Expose
    private Following_ following;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Integer followingUserId) {
        this.followingUserId = followingUserId;
    }

    public Integer getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Integer followerUserId) {
        this.followerUserId = followerUserId;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public Following_ getFollowing() {
        return following;
    }

    public void setFollowing(Following_ following) {
        this.following = following;
    }

}
