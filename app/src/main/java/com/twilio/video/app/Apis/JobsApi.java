package com.twilio.video.app.Apis;

import com.twilio.video.app.ChatUserResponse.ChatUserResponse;
import com.twilio.video.app.JobsResponse.FindJobsRespose;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JobsApi {

    @GET("jobs")
    Call<FindJobsRespose> getJobs(
            @Query("token") String token
    );

}
