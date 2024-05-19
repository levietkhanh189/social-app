package com.example.final_khang.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.final_khang.entity.Post;
import com.example.final_khang.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String createUserTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, email TEXT)";
        String createPostTable = "CREATE TABLE posts (id INTEGER PRIMARY KEY, title TEXT, content TEXT)";
        db.execSQL(createUserTable);
        db.execSQL(createPostTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS posts");
        onCreate(db);
    }

    public List<User> searchUsers(String query) {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE name LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                int id = cursor.getColumnIndex("id");
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM posts WHERE title LIKE ? OR content LIKE ?", new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post();
                int id = cursor.getColumnIndex("id");
                int UserId = cursor.getColumnIndex("email");
                int caption = cursor.getColumnIndex("name") ;
                if(id <0 || UserId <0 || caption <0)
                {
                    id =0;
                    UserId = 0;
                    caption = 0;
                }
                post.setId(cursor.getInt(id));
                post.setUserId(cursor.getInt(UserId));
                post.setCaption(cursor.getString(caption));
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
