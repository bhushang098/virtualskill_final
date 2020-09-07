package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.OTpResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OTpApi {

    @FormUrlEncoded
    @POST("verify_otp")
    Call<OTpResponse> verifyOTP(
            @Field("otp") String otp,
            @Field("token") String token
    );

    @FormUrlEncoded
    @GET("resend_otp")
    Call<OTpResponse> resendOTP(
            @Field("token") String token
    );
}
