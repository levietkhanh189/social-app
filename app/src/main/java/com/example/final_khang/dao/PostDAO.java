package com.example.final_khang.dao;

import android.content.Context;
import android.widget.Toast;

import com.example.final_khang.api.PostApi;
import com.example.final_khang.entity.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class PostDAO {
    private PostApi postApi;
    private Context context;

    public PostDAO(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postApi = retrofit.create(PostApi.class);
    }

    // Phương thức thêm một bài viết mới vào cơ sở dữ liệu
    public boolean insertPost(Post post) {
        Call<Post> call = postApi.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Post created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to create post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Phương thức xóa một bài viết khỏi cơ sở dữ liệu
    public boolean deletePost(int postId) {
        Call<Void> call = postApi.deletePost(postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Phương thức cập nhật thông tin của một bài viết trong cơ sở dữ liệu
    public boolean updatePost(Post post) {
        Call<Post> call = postApi.updatePost(post.getId(), post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Post updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Phương thức lấy tất cả các bài viết từ cơ sở dữ liệu
    public void getAllPosts(final PostListCallback callback) {
        Call<List<Post>> call = postApi.getAllPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
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

    // Phương thức lấy tất cả các bài viết của một người dùng từ cơ sở dữ liệu
    public void getAllPostsByUserId(int userId, final PostListCallback callback) {
        // Giả sử bạn có một API để lấy tất cả bài viết theo userId
        Call<List<Post>> call = postApi.getPostsByUserId(userId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
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

    // Define interfaces for callbacks
    public interface PostListCallback {
        void onSuccess(List<Post> posts);
        void onError(String error);
    }
}
