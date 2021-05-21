package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class ReqUnSubscribeAfter
{
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("following_user_id")
    private String following_user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFollowing_user_id() {
        return following_user_id;
    }

    public void setFollowing_user_id(String following_user_id) {
        this.following_user_id = following_user_id;
    }
}
