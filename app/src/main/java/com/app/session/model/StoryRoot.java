package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoryRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    StoryBody storyBody;


    public StoryBody getStoryBody() {
        return storyBody;
    }

    public void setStoryBody(StoryBody storyBody) {
        this.storyBody = storyBody;
    }

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


}
