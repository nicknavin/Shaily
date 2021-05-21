package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StoryData implements Parcelable
{
    private String language_cd;

    private String story_caption;

    private String story_id;

    private String story_text;

//    private String story_time;

    private String story_title;

    private String story_type;

    private String subscription_group_cd;

    private String thumbnail_text;

    private String user_cd;




    @SerializedName("subscription_id")
    public String subscriptionId;

    @SerializedName("story_type")
    public String storyType;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("story_text")
    public String storyText;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("story_title")
    public String storyTitle;

    @SerializedName("__v")
    public int V;

    @SerializedName("_id")
    public String id;

    @SerializedName("story_url")
    public String storyUrl;


    protected StoryData(Parcel in) {
        language_cd = in.readString();
        story_caption = in.readString();
        story_id = in.readString();
        story_text = in.readString();
        story_title = in.readString();
        story_type = in.readString();
        subscription_group_cd = in.readString();
        thumbnail_text = in.readString();
        user_cd = in.readString();
        subscriptionId = in.readString();
        storyType = in.readString();
        createdAt = in.readString();
        storyText = in.readString();
        userId = in.readString();
        storyTitle = in.readString();
        V = in.readInt();
        id = in.readString();
        storyUrl = in.readString();
    }

    public static final Creator<StoryData> CREATOR = new Creator<StoryData>() {
        @Override
        public StoryData createFromParcel(Parcel in) {
            return new StoryData(in);
        }

        @Override
        public StoryData[] newArray(int size) {
            return new StoryData[size];
        }
    };

    public void setLanguage_cd(String language_cd){
        this.language_cd = language_cd;
    }
    public String getLanguage_cd(){
        return this.language_cd;
    }
    public void setStory_caption(String story_caption){
        this.story_caption = story_caption;
    }
    public String getStory_caption(){
        return this.story_caption;
    }
    public void setStory_id(String story_id){
        this.story_id = story_id;
    }
    public String getStory_id(){
        return this.story_id;
    }
    public void setStory_text(String story_text){
        this.story_text = story_text;
    }
    public String getStory_text(){
        return this.story_text;
    }
//    public void setStory_time(String story_time){
//        this.story_time = story_time;
//    }
//    public String getStory_time(){
//        return this.story_time;
//    }
    public void setStory_title(String story_title){
        this.story_title = story_title;
    }
    public String getStory_title(){
        return this.story_title;
    }
    public void setStory_type(String story_type){
        this.story_type = story_type;
    }
    public String getStory_type(){
        return this.story_type;
    }
    public void setSubscription_group_cd(String subscription_group_cd){
        this.subscription_group_cd = subscription_group_cd;
    }
    public String getSubscription_group_cd(){
        return this.subscription_group_cd;
    }
    public void setThumbnail_text(String thumbnail_text){
        this.thumbnail_text = thumbnail_text;
    }
    public String getThumbnail_text(){
        return this.thumbnail_text;
    }
    public void setUser_cd(String user_cd){
        this.user_cd = user_cd;
    }
    public String getUser_cd(){
        return this.user_cd;
    }


    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getStoryType() {
        return storyType;
    }

    public void setStoryType(String storyType) {
        this.storyType = storyType;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(String storyUrl) {
        this.storyUrl = storyUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(language_cd);
        parcel.writeString(story_caption);
        parcel.writeString(story_id);
        parcel.writeString(story_text);
        parcel.writeString(story_title);
        parcel.writeString(story_type);
        parcel.writeString(subscription_group_cd);
        parcel.writeString(thumbnail_text);
        parcel.writeString(user_cd);
        parcel.writeString(subscriptionId);
        parcel.writeString(storyType);
        parcel.writeString(createdAt);
        parcel.writeString(storyText);
        parcel.writeString(userId);
        parcel.writeString(storyTitle);
        parcel.writeInt(V);
        parcel.writeString(id);
        parcel.writeString(storyUrl);
    }
}
