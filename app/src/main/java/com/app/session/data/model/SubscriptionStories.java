
package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubscriptionStories implements Parcelable {

    @SerializedName("__v")
    private Long _V;
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
    private SubscriptionId subscriptionId;
    @SerializedName("user_id")
    private UserDetails userId;
    @SerializedName("views")
    public String views;
    @SerializedName("daysAgo")
    public String daysAgo;
    @SerializedName("StoryViewed")
    public boolean storyViewed;

    @SerializedName("thumbnail_url")
    public String thumbnail_url;
    @SerializedName("display_doc_name")
    public String display_doc_name;

    @SerializedName("story_provider")
    public String story_provider;


    public SubscriptionStories() {

    }

    protected SubscriptionStories(Parcel in) {
        if (in.readByte() == 0) {
            _V = null;
        } else {
            _V = in.readLong();
        }
        _id = in.readString();
        createdAt = in.readString();
        storyText = in.readString();
        storyTitle = in.readString();
        storyType = in.readString();
        storyUrl = in.readString();
        subscriptionId = in.readParcelable(SubscriptionId.class.getClassLoader());
        userId = in.readParcelable(UserDetails.class.getClassLoader());
        views = in.readString();
        daysAgo = in.readString();
        storyViewed = in.readByte() != 0;
        thumbnail_url = in.readString();
        display_doc_name = in.readString();
        story_provider = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_V == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(_V);
        }
        dest.writeString(_id);
        dest.writeString(createdAt);
        dest.writeString(storyText);
        dest.writeString(storyTitle);
        dest.writeString(storyType);
        dest.writeString(storyUrl);
        dest.writeParcelable(subscriptionId, flags);
        dest.writeParcelable(userId, flags);
        dest.writeString(views);
        dest.writeString(daysAgo);
        dest.writeByte((byte) (storyViewed ? 1 : 0));
        dest.writeString(thumbnail_url);
        dest.writeString(display_doc_name);
        dest.writeString(story_provider);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscriptionStories> CREATOR = new Creator<SubscriptionStories>() {
        @Override
        public SubscriptionStories createFromParcel(Parcel in) {
            return new SubscriptionStories(in);
        }

        @Override
        public SubscriptionStories[] newArray(int size) {
            return new SubscriptionStories[size];
        }
    };


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

    public SubscriptionId getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(SubscriptionId subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public UserDetails getUserId() {
        return userId;
    }

    public void setUserId(UserDetails userId) {
        this.userId = userId;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getDaysAgo() {
        return daysAgo;
    }

    public void setDaysAgo(String daysAgo) {
        this.daysAgo = daysAgo;
    }

    public boolean isStoryViewed() {
        return storyViewed;
    }

    public void setStoryViewed(boolean storyViewed) {
        this.storyViewed = storyViewed;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
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

    @Override
    public String toString() {
        return "SubscriptionStories{" +
                "_V=" + _V +
                ", _id='" + _id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", storyText='" + storyText + '\'' +
                ", storyTitle='" + storyTitle + '\'' +
                ", storyType='" + storyType + '\'' +
                ", storyUrl='" + storyUrl + '\'' +
                ", subscriptionId=" + subscriptionId +
                ", userId=" + userId +
                ", views='" + views + '\'' +
                ", daysAgo='" + daysAgo + '\'' +
                ", storyViewed=" + storyViewed +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", display_doc_name='" + display_doc_name + '\'' +
                ", story_provider='" + story_provider + '\'' +
                '}';
    }
}
