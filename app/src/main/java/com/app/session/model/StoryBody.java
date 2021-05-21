package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;

public class StoryBody
{
    @SerializedName("totalOverAllStories")
    int totalOverAllStories;
    @SerializedName("Total_Page")
    int Total_Page;


    @SerializedName("getStoryModel")
    LinkedList<StoryModel> userStories;


    public int getTotalOverAllStories() {
        return totalOverAllStories;
    }

    public void setTotalOverAllStories(int totalOverAllStories) {
        this.totalOverAllStories = totalOverAllStories;
    }

    public LinkedList<StoryModel> getUserStories() {
        return userStories;
    }

    public void setUserStories(LinkedList<StoryModel> userStories) {
        this.userStories = userStories;
    }

    public int getTotal_Page() {
        return Total_Page;
    }

    public void setTotal_Page(int total_Page) {
        Total_Page = total_Page;
    }
}
