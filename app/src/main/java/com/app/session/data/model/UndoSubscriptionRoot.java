package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UndoSubscriptionRoot
{
    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private String body;

    @SerializedName("status")
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UndoSubscriptionRoot{" +
                "message='" + message + '\'' +
                ", body='" + body + '\'' +
                ", status=" + status +
                '}';
    }
}
