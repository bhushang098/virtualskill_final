package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {

    @POST("add_fmc")
    Call<MakeClassResponse> addFcmToken(
            @Query("fcm_token") String fcm_token,
            @Query("token") String token
    );
}
