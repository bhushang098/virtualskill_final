
package com.twilio.video.app.SingelUSerByIDResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleUserbyIDResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("is_following")
    @Expose
    private Integer is_following;

    @SerializedName("rating")
    @Expose
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public Integer getIs_following() {
        return is_following;
    }

    public void setIs_following(Integer is_following) {
        this.is_following = is_following;
    }
}
