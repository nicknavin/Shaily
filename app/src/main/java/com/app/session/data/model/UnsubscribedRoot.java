package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UnsubscribedRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private UnsubscribedBody unsubscribedBody;

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

    public UnsubscribedBody getUnsubscribedBody() {
        return unsubscribedBody;
    }

    public void setUnsubscribedBody(UnsubscribedBody unsubscribedBody) {
        this.unsubscribedBody = unsubscribedBody;
    }

    @Override
    public String toString() {
        return "UnsubscribedRoot{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", unsubscribedBody=" + unsubscribedBody +
                '}';
    }
}
