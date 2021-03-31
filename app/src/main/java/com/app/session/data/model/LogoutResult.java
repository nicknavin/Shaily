package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class LogoutResult {
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("body")
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
