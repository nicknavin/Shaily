package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryRoot {
    private int status;

    private String message;

    @SerializedName("body")
    private ArrayList<CategoryBody> body;

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

    public ArrayList<CategoryBody> getBody() {
        return body;
    }

    public void setBody(ArrayList<CategoryBody> body) {
        this.body = body;
    }
}
