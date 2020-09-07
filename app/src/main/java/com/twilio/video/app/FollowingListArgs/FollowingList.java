
package com.twilio.video.app.FollowingListArgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingList {

    @SerializedName("allow")
    @Expose
    private Integer allow;
    @SerializedName("follower_user_id")
    @Expose
    private Integer followerUserId;
    @SerializedName("following")
    @Expose
    private Following following;
    @SerializedName("following_user_id")
    @Expose
    private Integer followingUserId;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public Integer getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Integer followerUserId) {
        this.followerUserId = followerUserId;
    }

    public Following getFollowing() {
        return following;
    }

    public void setFollowing(Following following) {
        this.following = following;
    }

    public Integer getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Integer followingUserId) {
        this.followingUserId = followingUserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
