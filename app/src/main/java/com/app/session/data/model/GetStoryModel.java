
package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;

public class GetStoryModel {

    @SerializedName("createdAt")
    private String mCreatedAt;
    @SerializedName("story_text")
    private String mStoryText;
    @SerializedName("story_title")
    private String mStoryTitle;
    @SerializedName("story_type")
    private String mStoryType;
    @SerializedName("story_url")
    private String mStoryUrl;
    @SerializedName("subscription_id")
    private String mSubscriptionId;
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("__v")
    private Long m_V;
    @SerializedName("_id")
    private String m_id;

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getStoryText() {
        return mStoryText;
    }

    public void setStoryText(String storyText) {
        mStoryText = storyText;
    }

    public String getStoryTitle() {
        return mStoryTitle;
    }

    public void setStoryTitle(String storyTitle) {
        mStoryTitle = storyTitle;
    }

    public String getStoryType() {
        return mStoryType;
    }

    public void setStoryType(String storyType) {
        mStoryType = storyType;
    }

    public String getStoryUrl() {
        return mStoryUrl;
    }

    public void setStoryUrl(String storyUrl) {
        mStoryUrl = storyUrl;
    }

    public String getSubscriptionId() {
        return mSubscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        mSubscriptionId = subscriptionId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public Long get_V() {
        return m_V;
    }

    public void set_V(Long _V) {
        m_V = _V;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

}
