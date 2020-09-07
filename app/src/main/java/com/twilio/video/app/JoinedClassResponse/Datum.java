
package com.twilio.video.app.JoinedClassResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.video.app.ApiModals.Creator;

public class Datum {

    @SerializedName("e_id")
    @Expose
    private Integer eId;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("creator_id")
    @Expose
    private Integer creatorId;
    @SerializedName("host_status")
    @Expose
    private Integer hostStatus;
    @SerializedName("recurring_class")
    @Expose
    private Integer recurringClass;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("cover_path")
    @Expose
    private String coverPath;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
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
    @SerializedName("creator")
    @Expose
    private Creator creator;

    public Integer getEId() {
        return eId;
    }

    public void setEId(Integer eId) {
        this.eId = eId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getHostStatus() {
        return hostStatus;
    }

    public void setHostStatus(Integer hostStatus) {
        this.hostStatus = hostStatus;
    }

    public Integer getRecurringClass() {
        return recurringClass;
    }

    public void setRecurringClass(Integer recurringClass) {
        this.recurringClass = recurringClass;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
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

    public Integer geteId() {
        return eId;
    }

    public void seteId(Integer eId) {
        this.eId = eId;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
