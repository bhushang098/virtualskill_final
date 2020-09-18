
package com.twilio.video.app.NotificationAlertResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationAlerrtResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("new")
    @Expose
    private Integer total_new;

    public Integer getTotal_new() {
        return total_new;
    }

    public void setTotal_new(Integer total_new) {
        this.total_new = total_new;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
