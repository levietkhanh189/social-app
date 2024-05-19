package com.example.final_khang.api;
import com.example.final_khang.entity.Comment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface CommentApi {
    @GET("api/comments")
    Call<List<Comment>> getAllComments();

    @GET("api/comments/{id}")
    Call<Comment> getCommentById(@Path("id") int id);

    @GET("api/comments/post/{postId}")
    Call<List<Comment>> getCommentsByPostId(@Path("postId") int postId);

    @GET("api/comments/user/{userId}")
    Call<List<Comment>> getCommentsByUserId(@Path("userId") int userId);

    @POST("api/comments")
    Call<Comment> createComment(@Body Comment comment);

    @DELETE("api/comments/{id}")
    Call<Void> deleteComment(@Path("id") int id);
}

