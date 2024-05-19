package com.example.final_khang.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE))
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "caption")
    private String caption;

    @ColumnInfo(name = "image_uri")
    private byte[] imageUri;

    // Constructor
    public Post(int userId, String caption, byte[] imageUri) {
        this.userId = userId;
        this.caption = caption;
        this.imageUri = imageUri;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public byte[] getImageUri() {
        return imageUri;
    }

    public void setImageUri(byte[] imageUri) {
        this.imageUri = imageUri;
    }
}