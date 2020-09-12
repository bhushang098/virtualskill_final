package com.twilio.video.app.Apis;

import com.twilio.video.app.ApiModals.MakeClassResponse;
import com.twilio.video.app.ApiModals.MakeNewPostResponse;
import com.twilio.video.app.ApiModals.PostLikeResponse;
import com.twilio.video.app.HomePostModal.HomePostModal;
import com.twilio.video.app.UpdatePostResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface PostApi {

    @FormUrlEncoded
    @POST("home_posts")
    Call<HomePostModal> getHomePosts(
            @Field("token") String token
    );

    @Multipart
    @POST("new_post")
    Call<MakeNewPostResponse> makeNewPostByAPI(
            @Query("token") String token,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part image,
            @Part("link_youtube") RequestBody link_youtube,
            @Part MultipartBody.Part video,
            @Part("media_type") RequestBody media_type,
            @Part("group_id") RequestBody group_id,
            @Part("class_id") RequestBody class_id,
            @Part("skill_id") RequestBody skill_id
            );

    @FormUrlEncoded
    @POST("edit_post")
    Call<UpdatePostResponse> updatePost(
            @Query("token") String token,
            @Field("post_id") String Post_id,
            @Field("content") String content

    );

    @FormUrlEncoded
    @POST("delete_post")
    Call<MakeClassResponse> deletePost(
            @Query("token") String token,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("posts/comment")
    Call<MakeClassResponse> addComment(
            @Query("token") String token,
            @Field("id") String postId,
            @Field("comment") String Comment
    );


    @POST("home_posts")
    Call<HomePostModal> getPostByFilter(
            @Query("token") String token,
            @Query("type") String type,
            @Query("wall_id") String wallId

    );

    @FormUrlEncoded
    @POST("posts/like")
    Call<PostLikeResponse> likePost(
            @Query("token") String token,
            @Field("id") String id
    );
}
