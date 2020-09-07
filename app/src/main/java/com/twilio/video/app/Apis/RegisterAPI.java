package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.RegResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterAPI {

    @FormUrlEncoded
    @POST("register")
    Call<RegResponse> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );
}
