package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class VerifyRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    private VerifyBody body;

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

    public VerifyBody getBody() {
        return body;
    }

    public void setBody(VerifyBody body) {
        this.body = body;
    }
}
