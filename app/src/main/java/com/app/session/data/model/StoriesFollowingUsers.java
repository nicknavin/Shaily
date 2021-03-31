package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoriesFollowingUsers
{
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("load")
    private int load;

    public StoriesFollowingUsers()
    {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }
}
