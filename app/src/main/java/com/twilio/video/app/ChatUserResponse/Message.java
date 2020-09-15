
package com.twilio.video.app.ChatUserResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twilio.video.app.model.ExtrasInChatList;

public class Message {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("seen")
    @Expose
    private Integer seen;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("belongs_type")
    @Expose
    private String belongsType;
    @SerializedName("belongs_to")
    @Expose
    private String belongsTo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("dp")
    @Expose
    private String dp;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("sender")
    @Expose
    private Sender sender;

    @SerializedName("receiver_skill")
    @Expose
    private ExtrasInChatList receiver_skill;


    @SerializedName("receiver_team")
    @Expose
    private ExtrasInChatList receiver_team;


    public ExtrasInChatList getReceiver_skill() {
        return receiver_skill;
    }

    public void setReceiver_skill(ExtrasInChatList receiver_skill) {
        this.receiver_skill = receiver_skill;
    }

    public ExtrasInChatList getReceiver_team() {
        return receiver_team;
    }

    public void setReceiver_team(ExtrasInChatList receiver_team) {
        this.receiver_team = receiver_team;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSeen() {
        return seen;
    }

    public void setSeen(Integer seen) {
        this.seen = seen;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBelongsType() {
        return belongsType;
    }

    public void setBelongsType(String belongsType) {
        this.belongsType = belongsType;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
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

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

}
