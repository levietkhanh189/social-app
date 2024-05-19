package com.example.final_khang.data;
import android.content.Context;
import android.widget.Toast;

import com.example.final_khang.api.PostApi;
import com.example.final_khang.api.UserApi;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseHelper {

    private UserApi userApi;
    private PostApi postApi;
    private Context context;

    public DatabaseHelper(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
        postApi = retrofit.create(PostApi.class);
    }

    public void searchUsers(String query, final UserListCallback callback) {
        Call<List<User>> call = userApi.searchUsers(query);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get users");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void searchPosts(String query, final PostListCallback callback) {
        Call<List<Post>> call = postApi.searchPosts(query);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get posts");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void searchBoth(String query, final SearchCallback callback) {
        searchUsers(query, new UserListCallback() {
            @Override
            public void onSuccess(List<User> users) {
                searchPosts(query, new PostListCallback() {
                    @Override
                    public void onSuccess(List<Post> posts) {
                        List<Object> results = new ArrayList<>();
                        results.addAll(users);
                        results.addAll(posts);
                        callback.onSuccess(results);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Define interfaces for callbacks
    public interface UserListCallback {
        void onSuccess(List<User> users);
        void onError(String error);
    }

    public interface PostListCallback {
        void onSuccess(List<Post> posts);
        void onError(String error);
    }

    public interface SearchCallback {
        void onSuccess(List<Object> results);
        void onError(String error);
    }
}
