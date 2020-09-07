
package com.twilio.video.app.SingleClassResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wall {

    @SerializedName("new_post_class_id")
    @Expose
    private Integer newPostClassId;
    @SerializedName("new_post_group_id")
    @Expose
    private Integer newPostGroupId;
    @SerializedName("new_post_skill_id")
    @Expose
    private Integer newPostSkillId;

    public Integer getNewPostClassId() {
        return newPostClassId;
    }

    public void setNewPostClassId(Integer newPostClassId) {
        this.newPostClassId = newPostClassId;
    }

    public Integer getNewPostGroupId() {
        return newPostGroupId;
    }

    public void setNewPostGroupId(Integer newPostGroupId) {
        this.newPostGroupId = newPostGroupId;
    }

    public Integer getNewPostSkillId() {
        return newPostSkillId;
    }

    public void setNewPostSkillId(Integer newPostSkillId) {
        this.newPostSkillId = newPostSkillId;
    }

}
