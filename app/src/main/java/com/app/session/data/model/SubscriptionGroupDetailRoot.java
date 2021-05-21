package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class SubscriptionGroupDetailRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private SubscriptionGroup subscriptionGroup;


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

    public SubscriptionGroup getSubscriptionGroup() {
        return subscriptionGroup;
    }

    public void setSubscriptionGroup(SubscriptionGroup subscriptionGroup) {
        this.subscriptionGroup = subscriptionGroup;
    }
}
