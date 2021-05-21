package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserSubscriptionGroupsRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private ArrayList<UserSubscriptionGroupsBody> groupsBodyArrayList;

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

    public ArrayList<UserSubscriptionGroupsBody> getGroupsBodyArrayList() {
        return groupsBodyArrayList;
    }

    public void setGroupsBodyArrayList(ArrayList<UserSubscriptionGroupsBody> groupsBodyArrayList) {
        this.groupsBodyArrayList = groupsBodyArrayList;
    }
}
