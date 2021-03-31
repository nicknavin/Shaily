
package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class StoryModel {

    @SerializedName("_id")
    private String _id;
    @SerializedName("story_provider")
    private String story_provider;

    @SerializedName("subscription_id")
    @Nullable
   private ArrayList<SubscriptionId> subscriptionIds;


    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("daysAgo")
    private String daysAgo;
    @SerializedName("story_text")
    private String storyText;
    @SerializedName("story_title")
    private String storyTitle;
    @SerializedName("story_type")
    private String storyType;
    @SerializedName("story_url")
    private String storyUrl;
    @SerializedName("user_id")
    private UserDetails userDetails;
    @SerializedName("views")
    private String views;
    @SerializedName("thumbnail_url")
    private String thumbnail_url;
    @SerializedName("display_doc_name")
    private String display_doc_name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(String daysAgo) {
        this.daysAgo = daysAgo;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    @Nullable
    public ArrayList<SubscriptionId> getSubscriptionIds() {
        return subscriptionIds;
    }

    public void setSubscriptionIds(@Nullable ArrayList<SubscriptionId> subscriptionIds) {
        this.subscriptionIds = subscriptionIds;
    }

    public String getDisplay_doc_name() {
        return display_doc_name;
    }

    public void setDisplay_doc_name(String display_doc_name) {
        this.display_doc_name = display_doc_name;
    }

    public String getStory_provider() {
        return story_provider;
    }

    public void setStory_provider(String story_provider) {
        this.story_provider = story_provider;
    }
}
