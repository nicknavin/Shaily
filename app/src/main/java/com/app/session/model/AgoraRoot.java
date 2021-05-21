package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class AgoraRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private AgoraBody agoraBody;

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

    public AgoraBody getAgoraBody() {
        return agoraBody;
    }

    public void setAgoraBody(AgoraBody agoraBody) {
        this.agoraBody = agoraBody;
    }
}
