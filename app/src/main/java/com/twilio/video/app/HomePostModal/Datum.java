
package com.twilio.video.app.HomePostModal;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("name")
    @Expose
    private String name;
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
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("d_uploaded_files")
    @Expose
    private List<DUploadedFile> dUploadedFiles = null;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = null;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public List<DUploadedFile> getDUploadedFiles() {
        return dUploadedFiles;
    }

    public void setDUploadedFiles(List<DUploadedFile> dUploadedFiles) {
        this.dUploadedFiles = dUploadedFiles;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
