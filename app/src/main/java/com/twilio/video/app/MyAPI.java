package com.twilio.video.app;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyAPI {

    @FormUrlEncoded
    @POST("login")
    Call<Map> loginUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("submit") boolean submitFlag


    );
}
