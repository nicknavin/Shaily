package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class AgoraBody
{
    @SerializedName("token")
    private String token;
    @SerializedName("agorachannelName")
    private String agorachannelName;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAgorachannelName() {
        return agorachannelName;
    }

    public void setAgorachannelName(String agorachannelName) {
        this.agorachannelName = agorachannelName;
    }
}
