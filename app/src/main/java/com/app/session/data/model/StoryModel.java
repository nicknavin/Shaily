
package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class StoryModel implements Parcelable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("story_provider")
    private String story_provider;

    @SerializedName("subscription_id")
    @Nullable
    private ArrayList<SubscriptionId> subscriptionIds;


    @SerializedName("createdAt")
    private String createdAt;



    @SerializedName("story_type")
    private String storyType;

    @SerializedName("story_text")
    private String storyText;

    @SerializedName("story_title")
    private String storyTitle;



    @SerializedName("story_url")
    private String storyUrl;

    @SerializedName("thumbnail_url")
    private String thumbnail_url;


    @SerializedName("display_doc_name")
    private String display_doc_name;


    @SerializedName("views")
    private String views;


    @SerializedName("daysAgo")
    private String daysAgo;





    @SerializedName("user_id")
    private UserDetails userDetails;





    @SerializedName("read")
    @Nullable
    public StoryRead storyRead;


    public StoryModel()
    {

    }

    protected StoryModel(Parcel in) {
        _id = in.readString();
        story_provider = in.readString();
        subscriptionIds = in.createTypedArrayList(SubscriptionId.CREATOR);
        createdAt = in.readString();
        storyType = in.readString();
        storyText = in.readString();
        storyTitle = in.readString();
        storyUrl = in.readString();
        thumbnail_url = in.readString();
        display_doc_name = in.readString();
        views = in.readString();
        daysAgo = in.readString();
        userDetails = in.readParcelable(UserDetails.class.getClassLoader());
        storyRead = in.readParcelable(StoryRead.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(story_provider);
        dest.writeTypedList(subscriptionIds);
        dest.writeString(createdAt);
        dest.writeString(storyType);
        dest.writeString(storyText);
        dest.writeString(storyTitle);
        dest.writeString(storyUrl);
        dest.writeString(thumbnail_url);
        dest.writeString(display_doc_name);
        dest.writeString(views);
        dest.writeString(daysAgo);
        dest.writeParcelable(userDetails, flags);
        dest.writeParcelable(storyRead, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StoryModel> CREATOR = new Creator<StoryModel>() {
        @Override
        public StoryModel createFromParcel(Parcel in) {
            return new StoryModel(in);
        }

        @Override
        public StoryModel[] newArray(int size) {
            return new StoryModel[size];
        }
    };

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

    @Nullable
    public StoryRead getStoryRead() {
        return storyRead;
    }

    public void setStoryRead(@Nullable StoryRead storyRead) {
        this.storyRead = storyRead;
    }
}
