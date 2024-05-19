package com.example.final_khang.dao;
import android.content.Context;
import android.widget.Toast;

import com.example.final_khang.api.LikeApi;
import com.example.final_khang.entity.Like;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class LikeDAO {
    private LikeApi likeApi;
    private Context context;

    public LikeDAO(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        likeApi = retrofit.create(LikeApi.class);
    }

    // Hàm thêm like
    public boolean insertLike(Like like) {
        Call<Like> call = likeApi.createLike(like);
        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Like added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add like", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Hàm xóa like
    public boolean deleteLike(int userId, int postId) {
        Call<List<Like>> call = likeApi.getLikesByUserId(userId);
        call.enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Like like : response.body()) {
                        if (like.getPostId() == postId) {
                            Call<Void> deleteCall = likeApi.deleteLike(like.getId());
                            deleteCall.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "Like deleted successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Failed to delete like", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    // Hàm lấy số lượng like cho một post
    public void getLikeCount(int postId, final LikeCountCallback callback) {
        Call<List<Like>> call = likeApi.getLikesByPostId(postId);
        call.enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCount(response.body().size());
                } else {
                    callback.onError("Failed to get like count");
                }
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Hàm kiểm tra nếu user đã like post
    public void isPostLikedByUser(int userId, int postId, final LikeStatusCallback callback) {
        Call<List<Like>> call = likeApi.getLikesByUserId(userId);
        call.enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Like like : response.body()) {
                        if (like.getPostId() == postId) {
                            callback.onStatus(true);
                            return;
                        }
                    }
                    callback.onStatus(false);
                } else {
                    callback.onError("Failed to check like status");
                }
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Define interfaces for callbacks
    public interface LikeCountCallback {
        void onCount(int count);
        void onError(String error);
    }

    public interface LikeStatusCallback {
        void onStatus(boolean isLiked);
        void onError(String error);
    }
}

