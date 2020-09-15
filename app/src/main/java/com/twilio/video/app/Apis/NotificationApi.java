package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.NotificationAlertResponse.NotificationAlerrtResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationApi {

    @POST("add_fmc")
    Call<MakeClassResponse> addFcmToken(
            @Query("fcm_token") String fcm_token,
            @Query("token") String token
    );

    @GET("notification")
    Call<NotificationAlerrtResponse> getNotificationList
            (
                    @Query("token") String token
            );
}
