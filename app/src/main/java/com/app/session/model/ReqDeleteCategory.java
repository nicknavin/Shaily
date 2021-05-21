
package com.app.session.model;


import com.google.gson.annotations.SerializedName;

public class ReqDeleteCategory {

    @SerializedName("userCategory")
    private String mUserCategory;
    @SerializedName("user_id")
    private String mUserId;

    public String getUserCategory() {
        return mUserCategory;
    }

    public void setUserCategory(String userCategory) {
        mUserCategory = userCategory;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
