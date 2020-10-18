package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class StoryId {


    @SerializedName("story_id")
    private String story_id;
    @SerializedName("user_id")
    private String user_id;

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
}
