
package com.twilio.video.app.ApiModals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PpUploadResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_big")
    @Expose
    private String imageBig;
    @SerializedName("image_thumb")
    @Expose
    private String imageThumb;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageBig() {
        return imageBig;
    }

    public void setImageBig(String imageBig) {
        this.imageBig = imageBig;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

}
