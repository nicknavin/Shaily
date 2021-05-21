package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class RequestReadCountNormalStory
{
    @SerializedName("story_id")
    private String story_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("story_provider")
    private String story_provider;


    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStory_provider() {
        return story_provider;
    }

    public void setStory_provider(String story_provider) {
        this.story_provider = story_provider;
    }

    @Override
    public String toString() {
        return "RequestReadCountNormalStory{" +
                "story_id='" + story_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", story_provider='" + story_provider + '\'' +
                '}';
    }
}
