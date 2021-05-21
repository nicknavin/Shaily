package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubscriptionGroupDetailRoot {
    private int status;

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
