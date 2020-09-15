
package com.twilio.video.app.ClassesModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassMemberObj {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("event_id")
    @Expose
    private Integer eventId;
    @SerializedName("notified_on")
    @Expose
    private Object notifiedOn;
    @SerializedName("notified")
    @Expose
    private Integer notified;
    @SerializedName("follower_id")
    @Expose
    private Integer followerId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Object getNotifiedOn() {
        return notifiedOn;
    }

    public void setNotifiedOn(Object notifiedOn) {
        this.notifiedOn = notifiedOn;
    }

    public Integer getNotified() {
        return notified;
    }

    public void setNotified(Integer notified) {
        this.notified = notified;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
