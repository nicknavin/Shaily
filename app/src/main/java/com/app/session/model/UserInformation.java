package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class UserInformation {
    @SerializedName("userId")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
