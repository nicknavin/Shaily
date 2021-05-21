
package com.app.session.model;


import com.google.gson.annotations.SerializedName;



public class ReqSubscriptionStories {

    @SerializedName("subscription_id")
    private String subscriptionId;

    @SerializedName("user_id")
    private String user_id;


    @SerializedName("load")
    private String load;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
