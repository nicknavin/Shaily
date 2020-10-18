package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ITEGRITYPRO on 09-10-2018.
 */

public class AllStory implements Parcelable
{
    private String follower_user_cd;
    private String expert_user_cd;
    private String story_id;
    private String user_name;
    private String imgUrl;
    private String imageUrl;
    private String story_time;
    private String type;
    private String story_titel;
    private String story_text;
    private String story_caption;
    private String thumbnail_text;




public AllStory()
{

}


    protected AllStory(Parcel in) {
        follower_user_cd = in.readString();
        expert_user_cd = in.readString();
        story_id = in.readString();
        user_name = in.readString();
        imgUrl = in.readString();
        imageUrl = in.readString();
        story_time = in.readString();
        type = in.readString();
        story_titel = in.readString();
        story_text = in.readString();
        story_caption = in.readString();
        thumbnail_text = in.readString();
    }

    public static final Creator<AllStory> CREATOR = new Creator<AllStory>() {
        @Override
        public AllStory createFromParcel(Parcel in) {
            return new AllStory(in);
        }

        @Override
        public AllStory[] newArray(int size) {
            return new AllStory[size];
        }
    };

    public String getFollower_user_cd() {
        return follower_user_cd;
    }

    public void setFollower_user_cd(String follower_user_cd) {
        this.follower_user_cd = follower_user_cd;
    }

    public String getExpert_user_cd() {
        return expert_user_cd;
    }

    public void setExpert_user_cd(String expert_user_cd) {
        this.expert_user_cd = expert_user_cd;
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStory_time() {
        return story_time;
    }

    public void setStory_time(String story_time) {
        this.story_time = story_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStory_titel() {
        return story_titel;
    }

    public void setStory_titel(String story_titel) {
        this.story_titel = story_titel;
    }

    public String getStory_text() {
        return story_text;
    }

    public void setStory_text(String story_text) {
        this.story_text = story_text;
    }

    public String getStory_caption() {
        return story_caption;
    }

    public void setStory_caption(String story_caption) {
        this.story_caption = story_caption;
    }

    public String getThumbnail_text() {
        return thumbnail_text;
    }

    public void setThumbnail_text(String thumbnail_text) {
        this.thumbnail_text = thumbnail_text;
    }

    public static Creator<AllStory> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(follower_user_cd);
        dest.writeString(expert_user_cd);
        dest.writeString(story_id);
        dest.writeString(user_name);
        dest.writeString(imgUrl);
        dest.writeString(imageUrl);
        dest.writeString(story_time);
        dest.writeString(type);
        dest.writeString(story_titel);
        dest.writeString(story_text);
        dest.writeString(story_caption);
        dest.writeString(thumbnail_text);
    }
}
