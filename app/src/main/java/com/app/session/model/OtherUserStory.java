
package com.app.session.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OtherUserStory {

    @SerializedName("__v")
    private Long _V;
    @Expose
    private String _id;
    @Expose
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
    @Expose
    private Long views;

    public Long get_V() {
        return _V;
    }

    public void set_V(Long _V) {
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

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

}
