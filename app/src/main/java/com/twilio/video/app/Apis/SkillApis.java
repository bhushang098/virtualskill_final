package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.SkillItemResponse.SkillItemResponse;
import com.twilio.video.app.SkillSingleResponse.SkillSingleResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface SkillApis  {

    @FormUrlEncoded
    @POST("skill/create")
    // ClassREsponse NAd Skill Response Having Sanme Fileds
    Call<MakeClassResponse> makeNewSkill(
            @Query("token") String token,
            @Field("name") String name,
            @Field("who_can_post") String WhoCanPost,
            @Field("who_can_see") String WhoCanSee,
            @Field("fee") String fee,
            @Field("about") String about
    );

    @GET("skill/joined")
    Call<SkillItemResponse> getJoinedSkills(
            @Query("token") String token
    );


    @GET("skill/created")
    Call<SkillItemResponse> getHostedSkills(
            @Query("token") String token
    );

    @GET("skill/available")
    Call<SkillItemResponse> getAvailableSkills(
            @Query("token") String token
    );

    @GET()
    Call<SkillSingleResponse> getSingleSkill(
      @Url String skillId,
      @Query("token") String token
    );

    @GET()
    Call<MakeClassResponse> joinUnJoinSkill(
            @Url String SkillId,
            @Query("token") String token
    );


    @GET()
    Call<MakeClassResponse> deleteSkill(
            @Url String SkillId,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST()
    Call<MakeClassResponse> updateSkill(
            @Url String SkillId,
            @Query("token") String token,
            @Field("name") String name,
            @Field("who_can_post") String whoCanPost,
            @Field("who_can_see") String whoCanSee,
            @Field("fee") String fees,
            @Field("about") String about

    );
}
