package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.JobsResponse.FindJobsRespose;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JobsApi {

    @GET("jobs")
    Call<FindJobsRespose> getJobs(
            @Query("token") String token
    );

    @GET
    Call<MakeClassResponse> applyJob(
            @Url String url,
            @Query("token") String token

    );


}
