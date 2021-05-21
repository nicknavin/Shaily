package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class ReqPurchaseSubscriptionGroups
{
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("subscription_id")
    private String subscription_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }
}
