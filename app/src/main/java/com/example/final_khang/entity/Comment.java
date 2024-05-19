package com.example.final_khang.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments",
        foreignKeys = {
                @ForeignKey(entity = Post.class,
                        parentColumns = "id",
                        childColumns = "post_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "userID",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int idComment;

    @ColumnInfo(name = "post_id")
    private int postId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "comment_body")
    private String commentBody;

    // Constructors, getters, and setters
    public Comment(int postId, int userId, String commentBody) {
        this.postId = postId;
        this.userId = userId;
        this.commentBody = commentBody;
    }


    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }
}