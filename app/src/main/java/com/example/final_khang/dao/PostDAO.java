package com.example.final_khang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.Post;

public class PostDAO {
    SQLiteDatabase sqLiteDatabase;

    public PostDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.openDatabase();
    }

    // Phương thức thêm một bài viết mới vào cơ sở dữ liệu
    public boolean insertPost(Post post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("idUser", post.getUserId());
        contentValues.put("content", post.getCaption());
        contentValues.put("image", post.getImageUri());
        long result = sqLiteDatabase.insert("posts", null, contentValues);
        return result != -1; // Trả về true nếu thêm thành công, ngược lại trả về false
    }

    // Phương thức xóa một bài viết khỏi cơ sở dữ liệu
    public boolean deletePost(int postId) {
        int result = sqLiteDatabase.delete("posts", "id=?", new String[]{String.valueOf(postId)});
        return result > 0; // Trả về true nếu xóa thành công, ngược lại trả về false
    }

    // Phương thức cập nhật thông tin của một bài viết trong cơ sở dữ liệu
    public boolean updatePost(Post post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", post.getUserId());
        contentValues.put("caption", post.getCaption());
        contentValues.put("image_uri", post.getImageUri());

        int result = sqLiteDatabase.update("posts", contentValues, "id=?", new String[]{String.valueOf(post.getId())});
        return result > 0; // Trả về true nếu cập nhật thành công, ngược lại trả về false
    }

    // Phương thức lấy tất cả các bài viết từ cơ sở dữ liệu
    public Cursor getAllPosts() {
        return sqLiteDatabase.rawQuery("SELECT * FROM posts", null);
    }

    public Cursor getAllPostsByUserId(int userId) {
        return sqLiteDatabase.rawQuery("SELECT * FROM posts WHERE user_id=?", new String[]{String.valueOf(userId)});
    }
}