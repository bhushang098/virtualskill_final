
package com.twilio.video.app.FollowersListArgs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FolllowerListArgs {

    @SerializedName("followers_list")
    @Expose
    private List<FollowersList> followersList = null;

    public List<FollowersList> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<FollowersList> followersList) {
        this.followersList = followersList;
    }

}
