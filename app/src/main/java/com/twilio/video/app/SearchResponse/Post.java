
package com.twilio.video.app.SearchResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("skill_id")
    @Expose
    private Integer skillId;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("has_image")
    @Expose
    private Integer hasImage;
    @SerializedName("has_video")
    @Expose
    private Integer hasVideo;
    @SerializedName("youtube_link")
    @Expose
    private Object youtubeLink;
    @SerializedName("media_type")
    @Expose
    private Integer mediaType;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getHasImage() {
        return hasImage;
    }

    public void setHasImage(Integer hasImage) {
        this.hasImage = hasImage;
    }

    public Integer getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Integer hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Object getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(Object youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

}
