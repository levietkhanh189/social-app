package com.example.final_khang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SearchDAO {
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase getSqLiteDatabase;
    DBHelper dbHelper;

    public SearchDAO(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.openDatabase();
        getSqLiteDatabase = dbHelper.readDatabase();
    }

    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        Cursor cursor = getSqLiteDatabase.rawQuery("SELECT * FROM users WHERE name LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                int id = cursor.getColumnIndex("userid");
                int email = cursor.getColumnIndex("email");
                int username = cursor.getColumnIndex("name") ;
                if(id <0 || email <0 || username <0)
                {
                    id =0;
                    email = 0;
                    username = 0;
                }
                user.setEmail(cursor.getString(email));
                user.setUserID(cursor.getInt(id));
                user.setUserName(cursor.getString(username));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public List<Post> searchPosts(String query) {
        List<Post> posts = new ArrayList<>();
        String[] columns = {"id", "idUser", "content", "image"};
        String selection = "content LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        Cursor cursor = getSqLiteDatabase.query(
                "posts",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();
                post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                post.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("idUser")));
                post.setCaption(cursor.getString(cursor.getColumnIndexOrThrow("content")));
                posts.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return posts;
    }

    public List<Object> searchBoth(String query) {
        List<Object> results = new ArrayList<>();
        results.addAll(searchUsers(query));
        results.addAll(searchPosts(query));
        return results;
    }
}
