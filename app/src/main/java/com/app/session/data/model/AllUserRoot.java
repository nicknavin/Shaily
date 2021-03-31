
package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class AllUserRoot {

    @SerializedName("body")
    private ArrayList<AllUserBody> body;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;

    public ArrayList<AllUserBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<AllUserBody> body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
