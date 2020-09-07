package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.ClassJoinLeaveResponse;
import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.RoomJoinResponse;
import com.twilio.video.app.ClassesModal.Classes;
import com.twilio.video.app.CreatedClassResponse.CreatedClassRespones;
import com.twilio.video.app.JoinedClassResponse.JoinedClassRespones;
import com.twilio.video.app.SingleClassResponse.SingleClassResponse;

import java.sql.Date;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ClassesAPI {

    @GET("class/available")
    Call<Classes> getAvailableClasses(
            @Query("token") String token
    );

    @GET("class/joined")
    Call<JoinedClassRespones> getJoinedClasses(
            @Query("token") String token
    );

    @GET("class/created")
    Call<CreatedClassRespones> getCreatedClasses(
            @Query("token") String token
    );

    @GET
    Call<RoomJoinResponse> getRoomJoinToken
            (@Url String classId,
             @Query("token") String token
            );

    @GET
    Call<ClassJoinLeaveResponse> joinUnJoinClass
            (@Url String classId,
             @Query("token") String token);

    @FormUrlEncoded
    @POST("class/create")
    Call<MakeClassResponse> createNewClass (
            @Query("token") String token,
            @Field("name") String name,
            @Field("location") String location,
            @Field("start_time") String start_time,
            @Field("start_date") Date start_date,
            @Field("end_date") Date end_date,
            @Field("recurring_class") String recurring_class,
            @Field("duration") String duration,
            @Field("about") String about,
            @Field("free_class") String free_class,
            @Field("fee") String fee,
            @Field("submit") boolean submitFlag
            );


    @FormUrlEncoded
    @POST
    Call<MakeClassResponse> updateClassInfo
            (@Url String classId,
             @Query("token") String token,
             @Field("name") String name,
             @Field("location") String location,
             @Field("start_time") String start_time,
             @Field("start_date") Date start_date,
             @Field("end_date") Date end_date,
             @Field("recurring_class") String recurring_class,
             @Field("duration") String duration,
             @Field("about") String about,
             @Field("free_class") String free_class,
             @Field("fee") String fee,
             @Field("submit") boolean submitFlag
            );

    @GET
    Call<MakeClassResponse> deleteClass
            (@Url String classId,
             @Query("token") String token);

    @GET
    Call<SingleClassResponse> getSingleClass
            (@Url String classId,
             @Query("token") String token
            );

    @GET("end_class")
    Call<MakeClassResponse> endClass(
            @Query("token") String token
    );


}
