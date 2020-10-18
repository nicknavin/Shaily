
package com.app.session.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class SubscribedAllStroiesBody {

    @SerializedName("getStoryModel")
    private ArrayList<UserStory> subscriptionStories;
    @SerializedName("totalOverAllStories")
    private int mTotalOverAllStories;
    @SerializedName("Total_Page")
    private int Total_Page;






    public ArrayList<UserStory> getSubscriptionStories() {
        return subscriptionStories;
    }

    public void setSubscriptionStories(ArrayList<UserStory> subscriptionStories) {
        this.subscriptionStories = subscriptionStories;
    }

    public int getmTotalOverAllStories() {
        return mTotalOverAllStories;
    }

    public void setmTotalOverAllStories(int mTotalOverAllStories) {
        this.mTotalOverAllStories = mTotalOverAllStories;
    }

    public int getTotal_Page() {
        return Total_Page;
    }

    public void setTotal_Page(int total_Page) {
        Total_Page = total_Page;
    }
}
