package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.FollowerUserResponse.FollowerUserResponse;
import com.twilio.video.app.FollowingUserResponse.FollowingUserREsponse;
import com.twilio.video.app.SearchStudentResponse.SearchStudentResponse;
import com.twilio.video.app.SingelUSerByIDResponse.SingleUserbyIDResponse;
import com.twilio.video.app.SingleUserResponse.SingleUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserAPI {

    @GET("user")
    Call<SingleUserResponse> getUSer(
            @Query("token") String token
    );

    @GET("users/following")
    Call<FollowingUserREsponse> getFollowingUSers(
            @Query("token") String token
    );


    @GET("users/followers")
    Call<FollowerUserResponse> getFollowers(
            @Query("token") String token
    );

    @GET("user_search/students")
    Call<SearchStudentResponse> getSearchStudents(
            @Query("token") String token,
            @Query("page") String pageNo

    );

    @GET("user_search/professors")
    Call<SearchStudentResponse> getSearchPros(
            @Query("token") String token,
            @Query("page") String pageNo

    );

    @GET("user_search/hr")
    Call<SearchStudentResponse> getSearchHrs(
            @Query("token") String token,
            @Query("page") String pageNo

    );

    @GET()
    Call<SingleUserbyIDResponse> getUSerById(
            @Url String userId,
            @Query("token") String token
    );

    @GET()
    Call<MakeClassResponse> followOrUnFollow(
            @Url String userIdToToggle,
            @Query("token") String token

    );
}
