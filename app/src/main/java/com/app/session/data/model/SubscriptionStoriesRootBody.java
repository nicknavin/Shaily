package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

public class SubscriptionStoriesRootBody
{

    @SerializedName("totalOverAllStories")
    int totalOverAllStories;
    @SerializedName("Total_Page")
    int Total_Page;

    @SerializedName("getStoryModel")
    LinkedList<SubscriptionStories> subscriptionStories;


    public int getTotalOverAllStories() {
        return totalOverAllStories;
    }

    public void setTotalOverAllStories(int totalOverAllStories) {
        this.totalOverAllStories = totalOverAllStories;
    }

    public LinkedList<SubscriptionStories> getSubscriptionStories() {
        return subscriptionStories;
    }

    public void setSubscriptionStories(LinkedList<SubscriptionStories> subscriptionStories) {
        this.subscriptionStories = subscriptionStories;
    }


    public int getTotal_Page() {
        return Total_Page;
    }

    public void setTotal_Page(int total_Page) {
        Total_Page = total_Page;
    }
}
