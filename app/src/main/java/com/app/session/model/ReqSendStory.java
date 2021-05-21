package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReqSendStory implements Parcelable
{

    private String storyType;
    private String storyTitle;
    private String storyText;
    private String videoUrl;
    private String subscription_id;
    private String docFileName;
    private String docFilePath;

    public static Creator<ReqSendStory> getCREATOR() {
        return CREATOR;
    }

    public ReqSendStory() {

    }

    protected ReqSendStory(Parcel in) {
        storyType = in.readString();
        storyTitle = in.readString();
        storyText = in.readString();
        videoUrl = in.readString();
        subscription_id = in.readString();
        docFileName = in.readString();
        docFilePath = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(storyType);
        parcel.writeString(storyTitle);
        parcel.writeString(storyText);
        parcel.writeString(videoUrl);
        parcel.writeString(subscription_id);
        parcel.writeString(docFileName);
        parcel.writeString(docFilePath);
    }
}
