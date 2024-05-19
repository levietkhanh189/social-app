package com.example.final_khang.entity;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String email;
    private String password;
    private String userName;
    private byte[] image;

    private String bio;

    public User(String email, String password, String userName, byte[] image, String bio) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.image = image;
        this.bio = bio;
    }
    public User(){}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
