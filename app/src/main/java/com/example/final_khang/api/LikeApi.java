package com.example.final_khang.api;

import com.example.final_khang.entity.Like;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface LikeApi {
    @GET("api/likes")
    Call<List<Like>> getAllLikes();

    @GET("api/likes/{id}")
    Call<Like> getLikeById(@Path("id") int id);

    @GET("api/likes/user/{userId}")
    Call<List<Like>> getLikesByUserId(@Path("userId") int userId);

    @GET("api/likes/post/{postId}")
    Call<List<Like>> getLikesByPostId(@Path("postId") int postId);

    @POST("api/likes")
    Call<Like> createLike(@Body Like like);

    @DELETE("api/likes/{id}")
    Call<Void> deleteLike(@Path("id") int id);
}
