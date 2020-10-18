package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ConsultUserRoot
{
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private ArrayList<ConsultUser> consultUsersList;


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

    public ArrayList<ConsultUser> getConsultUsersList() {
        return consultUsersList;
    }

    public void setConsultUsersList(ArrayList<ConsultUser> consultUsersList) {
        this.consultUsersList = consultUsersList;
    }
}
