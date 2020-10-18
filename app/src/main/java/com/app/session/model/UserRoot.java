package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class UserRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    private UserBody userBody;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

    public UserBody getUserBody() {
        return userBody;
    }

    public void setUserBody(UserBody userBody) {
        this.userBody = userBody;
    }
}