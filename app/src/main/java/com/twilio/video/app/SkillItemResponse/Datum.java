
package com.twilio.video.app.SkillItemResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.video.app.ApiModals.Creator;
import com.twilio.video.app.TeamResponse.MemberObj;

import java.util.List;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("who_can_post")
    @Expose
    private String whoCanPost;
    @SerializedName("who_can_see")
    @Expose
    private String whoCanSee;
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
    @SerializedName("index_id")
    @Expose
    private Integer indexId;
    @SerializedName("skill_id")
    @Expose
    private Integer skillId;
    @SerializedName("follower_id")
    @Expose
    private Integer followerId;

    @SerializedName("creator")
    @Expose
    private Creator creator;

    @SerializedName("followers")
    @Expose
    private List<MemberObj> followers;

    public List<MemberObj> getFollowers() {
        return followers;
    }

    public void setFollowers(List<MemberObj> followers) {
        this.followers = followers;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
