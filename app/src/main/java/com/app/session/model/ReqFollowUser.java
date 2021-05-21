
package com.app.session.model;

import com.google.gson.annotations.SerializedName;



public class ReqFollowUser {

    @SerializedName("follower_user_id")
    private String mFollowerUserId;

    @SerializedName("user_id")
    private String mUserId;

    public String getFollowerUserId() {
        return mFollowerUserId;
    }

    public void setFollowerUserId(String followerUserId) {
        mFollowerUserId = followerUserId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
