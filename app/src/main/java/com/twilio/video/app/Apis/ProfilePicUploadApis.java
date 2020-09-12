package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.PpUploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
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
    @POST
    Call<PpUploadResponse> uploadProfilePic(
            @Url String appex,
            @Query("token") String token,
            @Part MultipartBody.Part image
    );

    @Multipart
    @Headers({
            "Accept: image/*",
            "Accept-Encoding: gzip",
    })
    @POST("class_upload_cover")
    Call<PpUploadResponse> uploadClassCover(
            @Query("token") String token,
            @Part("id") RequestBody classId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @Headers({
            "Accept: image/*",
            "Accept-Encoding: gzip",
    })


    @POST("team_upload_cover")
    Call<PpUploadResponse> uploadTeamCover(
            @Query("token") String token,
            @Part("id") RequestBody teamId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @Headers({
            "Accept: image/*",
            "Accept-Encoding: gzip",
    })

    @POST("skill_upload_cover")
    Call<PpUploadResponse> uploadSkillCover(
            @Query("token") String token,
            @Part("id") RequestBody skillId,
            @Part MultipartBody.Part image
    );
}
