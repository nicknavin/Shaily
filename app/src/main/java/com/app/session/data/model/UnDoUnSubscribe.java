package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UnDoUnSubscribe
{
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("undo_user_id")
    private String undo_user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUndo_user_id() {
        return undo_user_id;
    }

    public void setUndo_user_id(String undo_user_id) {
        this.undo_user_id = undo_user_id;
    }
}
