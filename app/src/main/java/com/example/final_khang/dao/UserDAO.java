package com.example.final_khang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_khang.data.DBHelper;
import com.example.final_khang.entity.User;

public class UserDAO {
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase getSqLiteDatabase;
    DBHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.openDatabase();
        getSqLiteDatabase = dbHelper.readDatabase();
    }

    public boolean insertUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.USER_EMAIL, user.getEmail());
        contentValues.put(dbHelper.USER_NAME, user.getUserName());
        contentValues.put(dbHelper.USER_PASSWORD, user.getPassword());
        contentValues.put(dbHelper.USER_BIO, user.getBio());
        contentValues.put(dbHelper.USER_IMAGE, user.getImage());
        long result = sqLiteDatabase.insert("users", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    // Hàm check Email có tồn tại hay chưa.
    public boolean checkEmailExists(String email) {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where email = ?", new String[]{email});
        if (cursor.getCount() > 0)
            return true;
        else return false;
    }

    // Hàm check Account user.
    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        Cursor cursor = getSqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(dbHelper.USER_ID);
                int emailIndex = cursor.getColumnIndex(dbHelper.USER_EMAIL);
                int passwordIndex = cursor.getColumnIndex(dbHelper.USER_PASSWORD);
                int nameIndex = cursor.getColumnIndex(dbHelper.USER_NAME);
                int imageIndex = cursor.getColumnIndex(dbHelper.USER_IMAGE);
                int bioIndex = cursor.getColumnIndex(dbHelper.USER_BIO);
                if (idIndex != -1 && emailIndex != -1 && passwordIndex != -1
                        && nameIndex != -1 && imageIndex != -1 && bioIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String emailUser = cursor.getString(emailIndex);
                    String passwordUser = cursor.getString(passwordIndex);
                    String nameUser = cursor.getString(nameIndex);
                    String bioUser = cursor.getString(bioIndex);
                    byte[] imageUser = cursor.getBlob(imageIndex);
                    user = new User(emailUser, passwordUser, nameUser, imageUser, bioUser);
                    user.setUserID(id);
                }
            }
        } finally {
            cursor.close();
        }
        return user;
    }

    // Hàm xóa tài khoản User
    public void deleteUser(int userID) {
        getSqLiteDatabase.delete(dbHelper.TABLE_USER, dbHelper.USER_ID + "=?",
                new String[]{String.valueOf(userID)});
    }

    // Hàm update user.
    public void updateUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.USER_NAME, user.getUserName());
        contentValues.put(dbHelper.USER_PASSWORD, user.getPassword());
        contentValues.put(dbHelper.USER_BIO, user.getBio());
        contentValues.put(dbHelper.USER_IMAGE, user.getImage());
        sqLiteDatabase.update(dbHelper.TABLE_USER, contentValues, dbHelper.USER_ID + "=?",
                new String[]{String.valueOf(user.getUserID())});
    }


    // Hàm select user theo userID
    public User getUserByID(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                dbHelper.TABLE_USER,
                new String[]{dbHelper.USER_ID, dbHelper.USER_NAME, dbHelper.USER_IMAGE},
                dbHelper.USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        User user = null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(dbHelper.USER_ID);
            int nameIndex = cursor.getColumnIndex(dbHelper.USER_NAME);
            int imageIndex = cursor.getColumnIndex(dbHelper.USER_IMAGE);
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            byte[] imageUser = cursor.getBlob(imageIndex);
            user = new User();
            user.setUserID(id);
            user.setUserName(name);
            user.setImage(imageUser);
        }
        cursor.close();
        db.close();
        return user;
    }
}
