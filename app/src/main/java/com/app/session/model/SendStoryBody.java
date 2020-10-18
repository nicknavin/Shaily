package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SendStoryBody implements Parcelable
{
    @SerializedName("subscription_id")
    private String subscription_id;
    @SerializedName("story_url")
    private String story_url;
    @SerializedName("views")
    private String views;
    @SerializedName("_id")
    private String _id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("story_type")
    private String story_type;
    @SerializedName("story_title")
    private String story_title;
    @SerializedName("story_text")
    private String story_text;
    @SerializedName("createdAt")
    private String createdAt;

    private String thumbnail_url;

    private String display_doc_name;

    @SerializedName("__v")
    private String __v;

    public SendStoryBody() {

    }

    protected SendStoryBody(Parcel in) {
        subscription_id = in.readString();
        story_url = in.readString();
        views = in.readString();
        _id = in.readString();
        user_id = in.readString();
        story_type = in.readString();
        story_title = in.readString();
        story_text = in.readString();
        createdAt = in.readString();
        thumbnail_url = in.readString();
        display_doc_name = in.readString();
        __v = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subscription_id);
        dest.writeString(story_url);
        dest.writeString(views);
        dest.writeString(_id);
        dest.writeString(user_id);
        dest.writeString(story_type);
        dest.writeString(story_title);
        dest.writeString(story_text);
        dest.writeString(createdAt);
        dest.writeString(thumbnail_url);
        dest.writeString(display_doc_name);
        dest.writeString(__v);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SendStoryBody> CREATOR = new Creator<SendStoryBody>() {
        @Override
        public SendStoryBody createFromParcel(Parcel in) {
            return new SendStoryBody(in);
        }

        @Override
        public SendStoryBody[] newArray(int size) {
            return new SendStoryBody[size];
        }
    };

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getStory_url() {
        return story_url;
    }

    public void setStory_url(String story_url) {
        this.story_url = story_url;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStory_type() {
        return story_type;
    }

    public void setStory_type(String story_type) {
        this.story_type = story_type;
    }

    public String getStory_title() {
        return story_title;
    }

    public void setStory_title(String story_title) {
        this.story_title = story_title;
    }

    public String getStory_text() {
        return story_text;
    }

    public void setStory_text(String story_text) {
        this.story_text = story_text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
