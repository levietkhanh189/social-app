package com.example.final_khang.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class DBHelper extends SQLiteOpenHelper {

    // TABLE USER.
    public static final String TABLE_USER = "users";
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_IMAGE = "image";
    public static final String USER_BIO = "bio";

    // TABLE POSTS.
    public static final String TABLE_POSTS = "posts";
    public static final String POST_ID = "id";
    public static final String POST_USER_ID = "idUser";
    public static final String POST_CONTENT = "content";
    public static final String POST_IMAGE = "image";

    // TABLE COMMENTS.
    public static final String TABLE_COMMENTS = "comments";
    public static final String COMMENT_ID = "idComment";
    public static final String COMMENT_POST_ID = "post_id";
    public static final String COMMENT_USER_ID = "user_id";
    public static final String COMMENT_BODY = "comment_body";

    // TABLE LIKES.
    public static final String TABLE_LIKES = "likes";
    public static final String LIKE_ID = "id";
    public static final String LIKE_USER_ID = "user_id";
    public static final String LIKE_POST_ID = "post_id";

    public static final String DBName = "Final.db";

    // Constructor
    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 2);
    }

    public SQLiteDatabase readDatabase() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase openDatabase() {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng User.
        String createTable_User = "CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PASSWORD + " TEXT, " +
                USER_BIO + " TEXT, " +
                USER_IMAGE + " BLOB" +
                ")";
        db.execSQL(createTable_User);

        // Tạo bảng Posts.
        String createTable_Posts = "CREATE TABLE " + TABLE_POSTS + " (" +
                POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                POST_USER_ID + " INTEGER, " +
                POST_CONTENT + " TEXT, " +
                POST_IMAGE + " BLOB, " +
                "FOREIGN KEY(" + POST_USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "))";
        db.execSQL(createTable_Posts);

        // Tạo bảng Comments.
        String createTable_Comments = "CREATE TABLE " + TABLE_COMMENTS + " (" +
                COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COMMENT_POST_ID + " INTEGER, " +
                COMMENT_USER_ID + " INTEGER, " +
                COMMENT_BODY + " TEXT, " +
                "FOREIGN KEY(" + COMMENT_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + POST_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COMMENT_USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + ") ON DELETE CASCADE)";
        db.execSQL(createTable_Comments);

        // Tạo bảng Likes.
        String createTable_Likes = "CREATE TABLE " + TABLE_LIKES + " (" +
                LIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LIKE_USER_ID + " INTEGER, " +
                LIKE_POST_ID + " INTEGER, " +
                "FOREIGN KEY(" + LIKE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "), " +
                "FOREIGN KEY(" + LIKE_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + POST_ID + "))";
        db.execSQL(createTable_Likes);

        //Setup TiengViet.
        db.execSQL("PRAGMA encoding = 'UTF-8';");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
        onCreate(db);
    }
}
