package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.PpUploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ProfilePicUploadApis {

    @Multipart
    @POST("upload_cover")
    Call<PpUploadResponse> uploadCoverPic(
            @Query("token") String token,
            @Part MultipartBody.Part image
    );
    @Multipart
    @POST("upload_pp")
    Call<PpUploadResponse> uploadProfilePic(
            @Query("token") String token,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST()
    Call<PpUploadResponse> uploadClassCover(
            @Url String classId,
            @Query("token") String token,
            @Part MultipartBody.Part image
    );
}
