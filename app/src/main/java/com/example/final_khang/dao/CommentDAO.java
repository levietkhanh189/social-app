package com.example.final_khang.dao;
import android.content.Context;
import android.widget.Toast;

import com.example.final_khang.api.CommentApi;
import com.example.final_khang.entity.Comment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class CommentDAO {
    private CommentApi commentApi;
    private Context context;

    public CommentDAO(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        commentApi = retrofit.create(CommentApi.class);
    }

    // Hàm thêm comment
    public boolean insertComment(Comment comment) {
        Call<Comment> call = commentApi.createComment(comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Comment added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Hàm lấy tất cả comment
    public void getAllComments(final CommentListCallback callback) {
        Call<List<Comment>> call = commentApi.getAllComments();
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get comments");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Hàm lấy comment theo post ID
    public void getCommentByIDPost(int postId, final CommentListCallback callback) {
        Call<List<Comment>> call = commentApi.getCommentsByPostId(postId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get comments by post ID");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Define interfaces for callbacks
    public interface CommentListCallback {
        void onSuccess(List<Comment> comments);
        void onError(String error);
    }
}
