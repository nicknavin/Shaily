package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

import androidx.annotation.Nullable;

public class SubscriptionStoriesRootBody
{

    @SerializedName("isPurchased")
    boolean isPurchased;
    @SerializedName("totalOverAllStories")
    int totalOverAllStories;
    @SerializedName("Total_Page")
    int Total_Page;




    @SerializedName("getStoryModel")
    @Nullable
    LinkedList<SubscriptionStories> subscriptionStories;


    public int getTotalOverAllStories() {
        return totalOverAllStories;
    }

    public void setTotalOverAllStories(int totalOverAllStories) {
        this.totalOverAllStories = totalOverAllStories;
    }

    @Nullable
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

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

}
