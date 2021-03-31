
package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;


public class ReqSubscriptionStories {

    @SerializedName("subscription_id")
    private String subscriptionId;

    @SerializedName("user_id")
    private String user_id;


    @SerializedName("load")
    private int load;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "ReqSubscriptionStories{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", user_id='" + user_id + '\'' +
                ", load=" + load +
                '}';
    }
}
