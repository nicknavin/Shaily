package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReqSendStory implements Parcelable
{

    private String storyType;
    private String storyProvider;
    private String storyTitle;
    private String storyText;
    private String videoUrl;
    private String subscription_id;
    private String docFileName;
    private String docFilePath;
    private String story_language_id;

    public ReqSendStory() {

    }

    protected ReqSendStory(Parcel in) {
        storyType = in.readString();
        storyProvider = in.readString();
        storyTitle = in.readString();
        storyText = in.readString();
        videoUrl = in.readString();
        subscription_id = in.readString();
        docFileName = in.readString();
        docFilePath = in.readString();
        story_language_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(storyType);
        dest.writeString(storyProvider);
        dest.writeString(storyTitle);
        dest.writeString(storyText);
        dest.writeString(videoUrl);
        dest.writeString(subscription_id);
        dest.writeString(docFileName);
        dest.writeString(docFilePath);
        dest.writeString(story_language_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReqSendStory> CREATOR = new Creator<ReqSendStory>() {
        @Override
        public ReqSendStory createFromParcel(Parcel in) {
            return new ReqSendStory(in);
        }

        @Override
        public ReqSendStory[] newArray(int size) {
            return new ReqSendStory[size];
        }
    };


    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
    }

    public String getStoryProvider() {
        return storyProvider;
    }

    public void setStoryProvider(String storyProvider) {
        this.storyProvider = storyProvider;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryText() {
        return storyText;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getDocFileName() {
        return docFileName;
    }

    public void setDocFileName(String docFileName) {
        this.docFileName = docFileName;
    }

    public String getDocFilePath() {
        return docFilePath;
    }

    public void setDocFilePath(String docFilePath) {
        this.docFilePath = docFilePath;
    }

    public String getStory_language_id() {
        return story_language_id;
    }

    public void setStory_language_id(String story_language_id) {
        this.story_language_id = story_language_id;
    }

    @Override
    public String toString() {
        return "ReqSendStory{" +
                "storyType='" + storyType + '\'' +
                ", storyProvider='" + storyProvider + '\'' +
                ", storyTitle='" + storyTitle + '\'' +
                ", storyText='" + storyText + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", subscription_id='" + subscription_id + '\'' +
                ", docFileName='" + docFileName + '\'' +
                ", docFilePath='" + docFilePath + '\'' +
                ", story_language_id='" + story_language_id + '\'' +
                '}';
    }
}
