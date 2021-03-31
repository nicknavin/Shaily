package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class FollowerUser
{
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("_id")
    private String _id;
    @SerializedName("login_user_id")
    private String login_user_id;
    @SerializedName("user_name")
    private String user_name;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
