package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginRoot
{
    private int status;

    private String message;

    @SerializedName("body")
    private LoginBody loginBody;

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

    public LoginBody getLoginBody() {
        return loginBody;
    }

    public void setLoginBody(LoginBody loginBody) {
        this.loginBody = loginBody;
    }
}