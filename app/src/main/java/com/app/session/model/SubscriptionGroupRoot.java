package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubscriptionGroupRoot {
    private int status;

    private String message;

    @SerializedName("body")
    private ArrayList<SubscriptionGroup> subscriptionGroupBodies;


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

    public ArrayList<SubscriptionGroup> getSubscriptionGroupBodies()
    {
        return subscriptionGroupBodies;
    }

    public void setSubscriptionGroupBodies(ArrayList<SubscriptionGroup> subscriptionGroupBodies)
    {
        this.subscriptionGroupBodies = subscriptionGroupBodies;
    }
}
