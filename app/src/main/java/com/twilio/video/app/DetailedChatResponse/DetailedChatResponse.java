
package com.twilio.video.app.DetailedChatResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailedChatResponse {

    @SerializedName("messages")
    @Expose
    private Messages messages;
    @SerializedName("haveElseMessages")
    @Expose
    private Boolean haveElseMessages;

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public Boolean getHaveElseMessages() {
        return haveElseMessages;
    }

    public void setHaveElseMessages(Boolean haveElseMessages) {
        this.haveElseMessages = haveElseMessages;
    }

}
