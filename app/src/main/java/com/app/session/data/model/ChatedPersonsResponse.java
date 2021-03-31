package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatedPersonsResponse
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;



    @SerializedName("body")
    private ArrayList <ChatedBody> chatedPersonsBody;

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

    public ArrayList<ChatedBody> getChatedPersonsBody() {
        return chatedPersonsBody;
    }

    public void setChatedPersonsBody(ArrayList<ChatedBody> chatedPersonsBody) {
        this.chatedPersonsBody = chatedPersonsBody;
    }


}
