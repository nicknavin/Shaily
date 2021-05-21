package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubscriptionStoriesRoot
{


    @SerializedName("body")
    SubscriptionStoriesRootBody subscriptionStoriesRootBody;

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

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


    public SubscriptionStoriesRootBody getSubscriptionStoriesRootBody() {
        return subscriptionStoriesRootBody;
    }

    public void setSubscriptionStoriesRootBody(SubscriptionStoriesRootBody subscriptionStoriesRootBody) {
        this.subscriptionStoriesRootBody = subscriptionStoriesRootBody;
    }
}
