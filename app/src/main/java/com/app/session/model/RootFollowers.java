package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RootFollowers
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private ArrayList<BodyFollowers> followerUsers;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<BodyFollowers> getFollowerUsers() {
        return followerUsers;
    }

    public void setFollowerUsers(ArrayList<BodyFollowers> followerUsers) {
        this.followerUsers = followerUsers;
    }
}
