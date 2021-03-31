package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginBody
{
    @SerializedName("token")
    private String token;

    @SerializedName("loginUser")
    private LoginUser loginUser;

    @SerializedName("Following")
    private int Following;

    @SerializedName("Followers")
    private int Followers;

    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
    public void setLoginUser(LoginUser loginUser){
        this.loginUser = loginUser;
    }
    public LoginUser getLoginUser(){
        return this.loginUser;
    }
    public void setFollowing(int Following){
        this.Following = Following;
    }
    public int getFollowing(){
        return this.Following;
    }
    public void setFollowers(int Followers){
        this.Followers = Followers;
    }
    public int getFollowers(){
        return this.Followers;
    }
}
