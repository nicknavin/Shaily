
package com.app.session.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RequestUpdateCategory {

    @Expose
    private String userCategory;
    @SerializedName("user_id")
    private String userId;

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
