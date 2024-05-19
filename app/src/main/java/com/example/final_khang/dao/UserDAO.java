package com.example.final_khang.dao;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.example.final_khang.api.UserApi;
import com.example.final_khang.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDAO {

    private UserApi userApi;
    private Context context;

    public UserDAO(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserApi.class);
    }
    public boolean insertUser(User user) {
        Call<User> call = userApi.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    public boolean checkEmailExists(String email) {
        final boolean[] exists = {false};
        Call<Boolean> call = userApi.checkEmailExists(email);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    exists[0] = response.body() != null && response.body();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return exists[0]; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    public User getUserByEmailAndPassword(String email, String password) {
        final User[] user = {null};
        Call<User> call = userApi.getUserByEmailAndPassword(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user[0] = response.body();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return user[0]; // Thay đổi này không thực sự phản ánh kết quả của gọi API, bạn có thể cải thiện bằng cách sử dụng Future hoặc LiveData
    }

    public void deleteUser(int userID) {
        Call<Void> call = userApi.deleteUser(userID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getUserByID(int userID, final UserCallback callback) {
        Call<User> call = userApi.getUserById(userID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to get user by ID");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateUser(User user) {
        Call<User> call = userApi.updateUser(user.getUserID(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }

}
