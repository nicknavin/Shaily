package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BodyFollowers {
    @SerializedName("follower_user_id")
    private FollowerUser followerUsers;

    public FollowerUser getFollowerUsers() {
        return followerUsers;
    }

    public void setFollowerUsers(FollowerUser followerUsers) {
        this.followerUsers = followerUsers;
    }
}
