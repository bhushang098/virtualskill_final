
package com.twilio.video.app.TeamResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.video.app.ApiModals.Creator;

public class Datum {

    @SerializedName("team_ids")
    @Expose
    private Integer teamIds;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("who_can_post")
    @Expose
    private String whoCanPost;
    @SerializedName("who_can_see")
    @Expose
    private String whoCanSee;
    @SerializedName("who_can_message")
    @Expose
    private String whoCanMessage;
    @SerializedName("creator_id")
    @Expose
    private String creatorId;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("cover_path")
    @Expose
    private String coverPath;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("follower_id")
    @Expose
    private Integer followerId;

    @SerializedName("created_by")
    @Expose
    private Creator created_by;

    public Integer getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(Integer teamIds) {
        this.teamIds = teamIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWhoCanPost() {
        return whoCanPost;
    }

    public void setWhoCanPost(String whoCanPost) {
        this.whoCanPost = whoCanPost;
    }

    public String getWhoCanSee() {
        return whoCanSee;
    }

    public void setWhoCanSee(String whoCanSee) {
        this.whoCanSee = whoCanSee;
    }

    public String getWhoCanMessage() {
        return whoCanMessage;
    }

    public void setWhoCanMessage(String whoCanMessage) {
        this.whoCanMessage = whoCanMessage;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public Creator getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Creator created_by) {
        this.created_by = created_by;
    }
}
