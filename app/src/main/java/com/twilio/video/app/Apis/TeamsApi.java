package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.CreatedClassResponse.CreatedClassRespones;
import com.twilio.video.app.SingleTeamResponse.SingleTeamResponse;
import com.twilio.video.app.TeamResponse.AvailAbleTeamsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TeamsApi {

    @GET("team/available")
    Call<AvailAbleTeamsResponse> getAvailableTeams(
            @Query("token") String token
    );


    @GET("team/joined")
    Call<AvailAbleTeamsResponse> getJoinedTeams(
            @Query("token") String token
    );

    @GET("team/created")
    Call<AvailAbleTeamsResponse> getCreatedTeams(
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("team/create")
    Call<MakeClassResponse> makeNewTeam(
            @Query("token") String token,
            @Field("name") String name,
            @Field("who_can_post") String whoCanPost,
            @Field("who_can_see") String whoCanSee,
            @Field("who_can_message") String whoCanMess,
            @Field("about") String about
    );
    @GET()
    Call<SingleTeamResponse> getSingleTeam(
            @Url String TeamId,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST()
    Call<MakeClassResponse> updateTeam(
            @Url String teamId,
            @Query("token") String token,
            @Field("who_can_post") String who_can_post,
            @Field("who_can_see") String who_can_see,
            @Field("who_can_message") String who_can_message,
            @Field("name") String name,
            @Field("about") String about
    );

    @GET()
    Call<MakeClassResponse> deleteTeam (
            @Url String teamId,
            @Query("token") String token
    );

    @GET()
    Call<MakeClassResponse> joinUnJoinTeam(
            @Url String teamId,
            @Query("token") String token
    );
}
