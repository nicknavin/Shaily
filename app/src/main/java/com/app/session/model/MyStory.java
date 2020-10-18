package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ITEGRITYPRO on 07-09-2018.
 */

public class MyStory implements Parcelable {


    private String story_id;
    private String user_cd;
    private String story_time;
    private String type;
    private String story_text;
    private String imgUrl;
    private String imageUrl;
    private String user_name;
    private String story_caption;
    private String story_title;
    private String group_user_cd;
    private String group_name;
    private String thumbnail_text;

    public  MyStory()
    {

    }
    protected MyStory(Parcel in) {
        story_id = in.readString();
        user_cd = in.readString();
        story_time = in.readString();
        type = in.readString();
        story_text = in.readString();
        imgUrl = in.readString();
        imageUrl = in.readString();
        user_name = in.readString();
        story_caption = in.readString();
        story_title = in.readString();
        group_user_cd = in.readString();
        group_name = in.readString();
        thumbnail_text = in.readString();
    }

    public static final Creator<MyStory> CREATOR = new Creator<MyStory>() {
        @Override
        public MyStory createFromParcel(Parcel in) {
            return new MyStory(in);
        }

        @Override
        public MyStory[] newArray(int size) {
            return new MyStory[size];
        }
    };

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
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

    public String getStory_text() {
        return story_text;
    }

    public void setStory_text(String story_text) {
        this.story_text = story_text;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStory_caption() {
        return story_caption;
    }

    public void setStory_caption(String story_caption) {
        this.story_caption = story_caption;
    }

    public String getStory_title() {
        return story_title;
    }

    public void setStory_title(String story_title) {
        this.story_title = story_title;
    }

    public String getGroup_user_cd() {
        return group_user_cd;
    }

    public void setGroup_user_cd(String group_user_cd) {
        this.group_user_cd = group_user_cd;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getThumbnail_text() {
        return thumbnail_text;
    }

    public void setThumbnail_text(String thumbnail_text) {
        this.thumbnail_text = thumbnail_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(story_id);
        dest.writeString(user_cd);
        dest.writeString(story_time);
        dest.writeString(type);
        dest.writeString(story_text);
        dest.writeString(imgUrl);
        dest.writeString(imageUrl);
        dest.writeString(user_name);
        dest.writeString(story_caption);
        dest.writeString(story_title);
        dest.writeString(group_user_cd);
        dest.writeString(group_name);
        dest.writeString(thumbnail_text);
    }
}

