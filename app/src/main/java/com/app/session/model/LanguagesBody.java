package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LanguagesBody
{
    @SerializedName("body")
    ArrayList<UserLangauges> userLangauges;

    public ArrayList<UserLangauges> getUserLangauges() {
        return userLangauges;
    }

    public void setUserLangauges(ArrayList<UserLangauges> userLangauges) {
        this.userLangauges = userLangauges;
    }
}
