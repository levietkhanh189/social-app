package com.example.final_khang.api;
import com.example.final_khang.entity.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi {
    @POST("api/posts")
    Call<Post> createPost(@Body Post post);
    @GET("api/posts/user/{userId}")
    Call<List<Post>> getPostsByUserId(@Path("userId") int userId);

    @GET("api/posts/search")
    Call<List<Post>> searchPosts(@Query("keyword") String keyword);

    @GET("api/posts")
    Call<List<Post>> getAllPosts();

    @PUT("api/posts/{id}")
    Call<Post> updatePost(@Path("id") int id, @Body Post post);

    @DELETE("api/posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}

