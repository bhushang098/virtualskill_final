
package com.twilio.video.app.FollowingListArgs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingListArgs {

    @SerializedName("following_list")
    @Expose
    private List<FollowingList> followingList = null;

    public List<FollowingList> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowingList> followingList) {
        this.followingList = followingList;
    }

}
