package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UserBody
{

    @SerializedName("user")
    private User user;

    @SerializedName("Following")
    private int Following;
    
    @SerializedName("Followers")
    private int Followers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
