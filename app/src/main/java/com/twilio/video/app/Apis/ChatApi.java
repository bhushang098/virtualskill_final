package com.twilio.video.app.Apis;

import com.twilio.video.app.ChatUserResponse.ChatUserResponse;
import com.twilio.video.app.DetailedChatResponse.DetailedChatResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ChatApi {

    @GET("short_chat_list")
    Call<ChatUserResponse> getChatUsers(
            @Query("token") String token
    );


    @FormUrlEncoded
    @POST("chat/get-messages")
    Call<DetailedChatResponse> getDetailedChatList(
            @Query("token") String token,
            @Field("participant") String id,
            @Field("chat_type") String chatType
    );

    @FormUrlEncoded
    @POST("chat/post-message")
    Call<Map> sendChatMess(
            @Query("token") String token,
            @Field("chat_type") String chatType,
            @Field("message") String chatMessage,
            @Field("participant") String participant,
            @Field("mobile") String one
    );

    @GET
    Call<ResponseBody> seenMessage(
            @Url String url,
            @Query("token") String token
    );
}
