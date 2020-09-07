
package com.twilio.video.app.ApiModals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeNewPostResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
