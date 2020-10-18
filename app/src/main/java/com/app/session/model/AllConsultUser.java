
package com.app.session.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Body;


public class AllConsultUser {

    @SerializedName("body")
    private ArrayList<Body> body;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;

    public ArrayList<Body> getBody() {
        return body;
    }

    public void setBody(ArrayList<Body> body) {
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
