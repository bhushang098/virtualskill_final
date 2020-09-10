package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.PpUploadResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
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
    @POST
    Call<PpUploadResponse> uploadClassCover(
            @Url String classId,
            @Query("token") String token,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST
    Call<PpUploadResponse> uploadTeamCover(
            @Url String tramId,
            @Query("token") String token,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST
    Call<PpUploadResponse> uploadSkillCover(
            @Url String skillId,
            @Query("token") String token,
            @Part MultipartBody.Part image
    );
}
