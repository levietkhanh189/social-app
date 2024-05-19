package com.example.final_khang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.Like;

public class LikeDAO {
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase getSqLiteDatabase;
    DBHelper dbHelper;

    public LikeDAO(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.openDatabase();
        getSqLiteDatabase = dbHelper.readDatabase();
    }

    // Hàm thêm like
    public boolean insertLike(Like like) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", like.getUserId());
        contentValues.put("post_id", like.getPostId());
        long result = sqLiteDatabase.insert("likes", null, contentValues);
        return result != -1;
    }

    // Hàm xóa like
    public boolean deleteLike(int userId, int postId) {
        return sqLiteDatabase.delete("likes", "user_id=? AND post_id=?", new String[]{String.valueOf(userId), String.valueOf(postId)}) > 0;
    }

    // Hàm lấy số lượng like cho một post
    public int getLikeCount(int postId) {
        Cursor cursor = getSqLiteDatabase.rawQuery("SELECT COUNT(*) FROM likes WHERE post_id=?", new String[]{String.valueOf(postId)});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        cursor.close();
        return 0;
    }

    // Hàm kiểm tra nếu user đã like post
    public boolean isPostLikedByUser(int userId, int postId) {
        Cursor cursor = getSqLiteDatabase.rawQuery("SELECT COUNT(*) FROM likes WHERE user_id=? AND post_id=?", new String[]{String.valueOf(userId), String.valueOf(postId)});
        if (cursor.moveToFirst()) {
            boolean isLiked = cursor.getInt(0) > 0;
            cursor.close();
            return isLiked;
        }
        cursor.close();
        return false;
    }
}
