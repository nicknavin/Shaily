package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class DemoReciver
{
    @SerializedName("uid")
    private String uid;


    public DemoReciver(String uid) {
        this.uid = uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "DemoReciver{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
