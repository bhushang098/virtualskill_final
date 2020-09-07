
package com.twilio.video.app.ChatUserResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatUserResponse {

    @SerializedName("total_new")
    @Expose
    private Integer totalNew;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public Integer getTotalNew() {
        return totalNew;
    }

    public void setTotalNew(Integer totalNew) {
        this.totalNew = totalNew;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
