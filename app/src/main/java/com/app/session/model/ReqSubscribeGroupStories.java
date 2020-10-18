
package com.app.session.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class ReqSubscribeGroupStories {

    @SerializedName("load")
    private String mLoad;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("subscription_id")
    private List<String> mSubscriptionId;

    public String getmLoad() {
        return mLoad;
    }

    public void setmLoad(String mLoad) {
        this.mLoad = mLoad;
    }

    public List<String> getmSubscriptionId() {
        return mSubscriptionId;
    }

    public void setmSubscriptionId(List<String> mSubscriptionId) {
        this.mSubscriptionId = mSubscriptionId;
    }

    public List<String> getSubscriptionId() {
        return mSubscriptionId;
    }

    public void setSubscriptionId(List<String> subscriptionId) {
        mSubscriptionId = subscriptionId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
