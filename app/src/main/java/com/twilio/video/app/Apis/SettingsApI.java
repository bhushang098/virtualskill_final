package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.SettingsResponse.SettingsResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SettingsApI {

    @FormUrlEncoded
    @POST("user/edit_info")
    Call<SettingsResponse> updateBasicInfo(
            @Query("token") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("skill") String skill,
            @Field("interests") List<String> interests,
            @Field("location") String location,
            @Field("gender") String gender
            );

    @FormUrlEncoded
    @POST("user/username_edit")
    Call<SettingsResponse> updateUserName(
            @Query("token") String token,
            @Field("username") String userName
    );

    @GET("apply_for_pro")
    Call<SettingsResponse> applyPro(
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("user/change_pass")
    Call<SettingsResponse> updatePass(
            @Query("token") String token,
            @Field("current_password") String current_password,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @FormUrlEncoded
    @POST("forget_password")
    Call<MakeClassResponse> getOTp(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("user/change_password_by_otp")
    Call<MakeClassResponse> changePassByOtp(
            @Field("otp") String otp,
            @Field("phone") String phone,
            @Field("password") String Pass1,
            @Field("password_confirmation") String pass2
    );

    @GET("has_update")
    Call<Map<String,Integer>> hasUpdate(
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("add/rating")
    Call<MakeClassResponse>  setProRating(
            @Query("token") String token,
            @Field("teacher_id") String teacherId,
            @Field("rating") String rating,
            @Field("review") String review
    );
}
