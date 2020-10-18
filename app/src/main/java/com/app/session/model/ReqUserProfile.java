
package com.app.session.model;


import com.google.gson.annotations.SerializedName;


public class ReqUserProfile {

    @SerializedName("person_user_id")
    private String personUserId;
    @SerializedName("user_id")
    private String userId;

    public String getPersonUserId() {
        return personUserId;
    }

    public void setPersonUserId(String personUserId) {
        this.personUserId = personUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
