package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ITEGRITYPRO on 26-09-2018.
 */

public class StoryGroup implements Parcelable
{
    private String user_cd;
    private String group_user_cd;
    private String group_name;
    private String group_desc;
    private String group_url_image_address;
    private String fee;

    protected StoryGroup(Parcel in) {
        user_cd = in.readString();
        group_user_cd = in.readString();
        group_name = in.readString();
        group_desc = in.readString();
        group_url_image_address = in.readString();
        fee = in.readString();
    }
   public StoryGroup()
    {

    }

    public static final Creator<StoryGroup> CREATOR = new Creator<StoryGroup>() {
        @Override
        public StoryGroup createFromParcel(Parcel in) {
            return new StoryGroup(in);
        }

        @Override
        public StoryGroup[] newArray(int size) {
            return new StoryGroup[size];
        }
    };

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
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

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getGroup_url_image_address() {
        return group_url_image_address;
    }

    public void setGroup_url_image_address(String group_url_image_address) {
        this.group_url_image_address = group_url_image_address;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_cd);
        dest.writeString(group_user_cd);
        dest.writeString(group_name);
        dest.writeString(group_desc);
        dest.writeString(group_url_image_address);
        dest.writeString(fee);
    }
}
