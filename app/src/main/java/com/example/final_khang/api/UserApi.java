package com.example.final_khang.api;
import com.example.final_khang.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @POST("api/users")
    Call<User> createUser(@Body User user);

    @GET("api/users/{email}")
    Call<Boolean> checkEmailExists(@Path("email") String email);

    @GET("api/users/search")
    Call<List<User>> searchUsers(@Query("keyword") String keyword);

    @GET("api/users/{email}/{password}")
    Call<User> getUserByEmailAndPassword(@Path("email") String email, @Path("password") String password);

    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("api/users")
    Call<List<User>> getAllUsers();

    @DELETE("api/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @PUT("api/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
}


