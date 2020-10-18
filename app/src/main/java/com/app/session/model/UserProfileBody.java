package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserProfileBody
{

    @SerializedName("user")
    private OtherUserProfile user;

    @SerializedName("userStories")
    private ArrayList<PersonalProfileStory> personalProfileStoryArrayList;

    @SerializedName("Following_count")
    private int Following;
    
    @SerializedName("Followers_count")
    private int Followers;

    @SerializedName("isFollow")
    private boolean isFollow;

    public OtherUserProfile getUser() {
        return user;
    }

    public void setUser(OtherUserProfile user) {
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

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public ArrayList<PersonalProfileStory> getPersonalProfileStoryArrayList() {
        return personalProfileStoryArrayList;
    }

    public void setPersonalProfileStoryArrayList(ArrayList<PersonalProfileStory> personalProfileStoryArrayList) {
        this.personalProfileStoryArrayList = personalProfileStoryArrayList;
    }
}
