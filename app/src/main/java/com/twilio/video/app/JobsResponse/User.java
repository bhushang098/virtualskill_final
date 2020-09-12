
package com.twilio.video.app.JobsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("class_status")
    @Expose
    private Object classStatus;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("is_hr")
    @Expose
    private Integer isHr;
    @SerializedName("last_class_joined")
    @Expose
    private Object lastClassJoined;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("is_admin")
    @Expose
    private Integer isAdmin;
    @SerializedName("can_create_team")
    @Expose
    private Integer canCreateTeam;
    @SerializedName("can_create_skill")
    @Expose
    private Integer canCreateSkill;
    @SerializedName("can_create_class")
    @Expose
    private Integer canCreateClass;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("private")
    @Expose
    private Integer _private;
    @SerializedName("birthday")
    @Expose
    private Object birthday;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("bio")
    @Expose
    private Object bio;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("cover_path")
    @Expose
    private String coverPath;
    @SerializedName("is_phone_verified")
    @Expose
    private Integer isPhoneVerified;
    @SerializedName("otp_purpose")
    @Expose
    private Integer otpPurpose;
    @SerializedName("pro_requested_at")
    @Expose
    private String proRequestedAt;
    @SerializedName("skill")
    @Expose
    private Object skill;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("interests")
    @Expose
    private Object interests;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Object getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(Object classStatus) {
        this.classStatus = classStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getIsHr() {
        return isHr;
    }

    public void setIsHr(Integer isHr) {
        this.isHr = isHr;
    }

    public Object getLastClassJoined() {
        return lastClassJoined;
    }

    public void setLastClassJoined(Object lastClassJoined) {
        this.lastClassJoined = lastClassJoined;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getCanCreateTeam() {
        return canCreateTeam;
    }

    public void setCanCreateTeam(Integer canCreateTeam) {
        this.canCreateTeam = canCreateTeam;
    }

    public Integer getCanCreateSkill() {
        return canCreateSkill;
    }

    public void setCanCreateSkill(Integer canCreateSkill) {
        this.canCreateSkill = canCreateSkill;
    }

    public Integer getCanCreateClass() {
        return canCreateClass;
    }

    public void setCanCreateClass(Integer canCreateClass) {
        this.canCreateClass = canCreateClass;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
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

    public Integer getPrivate() {
        return _private;
    }

    public void setPrivate(Integer _private) {
        this._private = _private;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getBio() {
        return bio;
    }

    public void setBio(Object bio) {
        this.bio = bio;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Integer getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(Integer isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public Integer getOtpPurpose() {
        return otpPurpose;
    }

    public void setOtpPurpose(Integer otpPurpose) {
        this.otpPurpose = otpPurpose;
    }

    public String getProRequestedAt() {
        return proRequestedAt;
    }

    public void setProRequestedAt(String proRequestedAt) {
        this.proRequestedAt = proRequestedAt;
    }

    public Object getSkill() {
        return skill;
    }

    public void setSkill(Object skill) {
        this.skill = skill;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getInterests() {
        return interests;
    }

    public void setInterests(Object interests) {
        this.interests = interests;
    }

}
