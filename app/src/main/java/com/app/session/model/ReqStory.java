
package com.app.session.model;


import com.google.gson.annotations.SerializedName;



public class ReqStory {

    @SerializedName("load")
    private String mLoad;
    @SerializedName("user_id")
    private String mUserId;


    public String getmLoad() {
        return mLoad;
    }

    public void setmLoad(String mLoad) {
        this.mLoad = mLoad;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
