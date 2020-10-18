package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LanguagesRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    ArrayList<Language> userLangauges;

    public ArrayList<Language> getUserLangauges() {
        return userLangauges;
    }

    public void setUserLangauges(ArrayList<Language> userLangauges) {
        this.userLangauges = userLangauges;
    }

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


}
