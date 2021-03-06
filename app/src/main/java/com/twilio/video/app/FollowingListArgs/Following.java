
package com.twilio.video.app.FollowingListArgs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Following {

    @SerializedName("private")
    @Expose
    private Integer _private;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("can_create_class")
    @Expose
    private Integer canCreateClass;
    @SerializedName("can_create_skill")
    @Expose
    private Integer canCreateSkill;
    @SerializedName("can_create_team")
    @Expose
    private Integer canCreateTeam;
    @SerializedName("cover_path")
    @Expose
    private String coverPath;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("interests")
    @Expose
    private String interests;
    @SerializedName("is_admin")
    @Expose
    private Integer isAdmin;
    @SerializedName("is_phone_verified")
    @Expose
    private Integer isPhoneVerified;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("otp_purpose")
    @Expose
    private Integer otpPurpose;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("pro_requested_at")
    @Expose
    private String proRequestedAt;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("skill")
    @Expose
    private String skill;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_type")
    @Expose
    private Integer userType;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("class_status")
    @Expose
    private String classStatus;
    @SerializedName("last_class_joined")
    @Expose
    private Double lastClassJoined;

    @SerializedName("rating")
    @Expose
    private String rating;

    public Integer get_private() {
        return _private;
    }

    public void set_private(Integer _private) {
        this._private = _private;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getPrivate() {
        return _private;
    }

    public void setPrivate(Integer _private) {
        this._private = _private;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getCanCreateClass() {
        return canCreateClass;
    }

    public void setCanCreateClass(Integer canCreateClass) {
        this.canCreateClass = canCreateClass;
    }

    public Integer getCanCreateSkill() {
        return canCreateSkill;
    }

    public void setCanCreateSkill(Integer canCreateSkill) {
        this.canCreateSkill = canCreateSkill;
    }

    public Integer getCanCreateTeam() {
        return canCreateTeam;
    }

    public void setCanCreateTeam(Integer canCreateTeam) {
        this.canCreateTeam = canCreateTeam;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(Integer isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOtpPurpose() {
        return otpPurpose;
    }

    public void setOtpPurpose(Integer otpPurpose) {
        this.otpPurpose = otpPurpose;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProRequestedAt() {
        return proRequestedAt;
    }

    public void setProRequestedAt(String proRequestedAt) {
        this.proRequestedAt = proRequestedAt;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassStatus() {
        return classStatus;
    }

    public void setClassStatus(String classStatus) {
        this.classStatus = classStatus;
    }

    public Double getLastClassJoined() {
        return lastClassJoined;
    }

    public void setLastClassJoined(Double lastClassJoined) {
        this.lastClassJoined = lastClassJoined;
    }

}
