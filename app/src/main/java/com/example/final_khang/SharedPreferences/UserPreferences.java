package com.example.final_khang.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.final_khang.entity.User;

public class UserPreferences {
    private static final String SHARED_PREFS_NAME = "userInfor";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PASSWORD = "userPassword";
    private static final String KEY_USER_IMAGE = "userImage";
    private static final String KEY_USER_BIO = "userBio";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserData(Context context, User user) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getUserID());
        editor.putString(KEY_USER_NAME, user.getUserName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_USER_BIO, user.getBio());
        editor.putString(KEY_USER_IMAGE, Base64.encodeToString(user.getImage(), Base64.DEFAULT));
        editor.apply();
    }

    public static User getUserData(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);
        String userName = sharedPreferences.getString(KEY_USER_NAME, null);
        String userEmail = sharedPreferences.getString(KEY_USER_EMAIL, null);
        String userPass = sharedPreferences.getString(KEY_USER_PASSWORD, null);
        String userBio = sharedPreferences.getString(KEY_USER_BIO, null);
        String userImageBase64 = sharedPreferences.getString(KEY_USER_IMAGE, null);
        byte[] userImage = null;
        if (userImageBase64 != null) {
            try {
                userImage = Base64.decode(userImageBase64, Base64.DEFAULT);
            } catch (IllegalArgumentException e) {
                // Handle the base-64 decoding error
                e.printStackTrace();
                userImage = null;
            }
        }

        if (userId != -1 && userName != null && userEmail != null) {
            User user = new User(userEmail, userPass, userName, userImage, userBio);
            user.setUserID(userId);
            return user;
        } else {
            return null;
        }
    }

    public static void clearUserData(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
