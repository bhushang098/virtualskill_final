package com.twilio.video.app.Apis;

import com.twilio.video.app.SearchResponse.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SearchApi{


    @FormUrlEncoded
    @POST("search")
    Call<SearchResponse> searchByKey(
            @Query("token") String token,
            @Field("key") String key
    );


}
