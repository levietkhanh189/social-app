package com.example.final_khang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.Comment;
import com.example.final_khang.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase getSqLiteDatabase;
    DBHelper dbHelper;

    public CommentDAO(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.openDatabase();
        getSqLiteDatabase = dbHelper.readDatabase();
    }

    // hàm thêm comment.
    public boolean insertComment(Comment comment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("post_id", comment.getPostId());
        contentValues.put("user_id", comment.getUserId());
        contentValues.put("comment_body", comment.getCommentBody());
        long result = sqLiteDatabase.insert("comments", null, contentValues);
        if (result == -1) return false;
        else return true;
    }


    public Cursor getAllComments() {
        return sqLiteDatabase.rawQuery("SELECT * FROM comments", null);
    }
    public Cursor getCommentByIDPost(int postId) {
        return getSqLiteDatabase.rawQuery("SELECT * FROM comments WHERE post_id = ?", new String[]{String.valueOf(postId)});
    }
}
