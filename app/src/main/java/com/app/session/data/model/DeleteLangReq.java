package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class DeleteLangReq
{
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("userLangauges")
    private String userLangauges;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserLangauges() {
        return userLangauges;
    }

    public void setUserLangauges(String userLangauges) {
        this.userLangauges = userLangauges;
    }
}
