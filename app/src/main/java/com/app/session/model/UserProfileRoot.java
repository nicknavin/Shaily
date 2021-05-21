package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class UserProfileRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    private UserProfileBody userBody;

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

    public UserProfileBody getUserBody() {
        return userBody;
    }

    public void setUserBody(UserProfileBody userBody) {
        this.userBody = userBody;
    }
}