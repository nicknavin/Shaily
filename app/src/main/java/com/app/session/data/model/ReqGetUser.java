package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class ReqGetUser
{
    @SerializedName("category_id")
    private String category_id;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
