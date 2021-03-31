package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class SearchUser
{
    @SerializedName("search_text")
    private String searchUser;

    public String getSearchUser() {
        return searchUser;
    }

    public void setSearchUser(String searchUser) {
        this.searchUser = searchUser;
    }
}
