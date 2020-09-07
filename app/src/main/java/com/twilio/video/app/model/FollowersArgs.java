package com.twilio.video.app.model;

import com.twilio.video.app.SingelUSerByIDResponse.Follower;
import com.twilio.video.app.SingelUSerByIDResponse.Following;

import java.util.ArrayList;
import java.util.List;

public class FollowersArgs {

    public List<Follower> followers = new ArrayList<Follower>();
    public List<Following> following = new ArrayList<Following>();

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Following> getFollowing() {
        return following;
    }

    public void setFollowing(List<Following> following) {
        this.following = following;
    }
}
