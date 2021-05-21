package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UserId
 {

    @SerializedName("user_id")
    private String user_id;

    public UserId() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

     @Override
     public String toString() {
         return "UserId{" +
                 "user_id='" + user_id + '\'' +
                 '}';
     }
 }
