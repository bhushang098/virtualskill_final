
package com.twilio.video.app.FollowerUserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

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
    @SerializedName("follower")
    @Expose
    private Follower follower;

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

    public Follower getFollower() {
        return follower;
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

}
