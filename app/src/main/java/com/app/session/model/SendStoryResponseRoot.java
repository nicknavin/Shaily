package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class SendStoryResponseRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private SendStoryBody sendStoryBody;


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

    public SendStoryBody getSendStoryBody() {
        return sendStoryBody;
    }

    public void setSendStoryBody(SendStoryBody sendStoryBody) {
        this.sendStoryBody = sendStoryBody;
    }
}
