
package com.app.session.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PersonalProfileStory {

    @SerializedName("__v")
    private String _V;
    @SerializedName("_id")
    private String _id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("story_text")
    private String storyText;
    @SerializedName("story_title")
    private String storyTitle;
    @SerializedName("story_type")
    private String storyType;
    @SerializedName("story_url")
    private String storyUrl;
    @SerializedName("subscription_id")
    private String subscriptionId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("views")
    private String views;

    public String get_V() {
        return _V;
    }

    public void set_V(String _V) {
        this._V = _V;
    }

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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }
}
