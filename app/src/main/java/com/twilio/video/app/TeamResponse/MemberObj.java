
package com.twilio.video.app.TeamResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberObj {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("follower_id")
    @Expose
    private Integer followerId;
    @SerializedName("last_seen")
    @Expose
    private String lastSeen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

}
