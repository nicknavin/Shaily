package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ITEGRITYPRO on 23-03-2018.
 */

public class Brief_CV implements Parcelable
{

    @SerializedName("language_id")
    UserLangauges language_id;

//    @SerializedName("language_cd")
    private String language_cd;

    private String native_name;

    @SerializedName("brief_cv")
    private String brief_cv;

    private boolean isChecked;
    private String english_name;

    @SerializedName("video_url")
    private String video_url;

    @SerializedName("video_thumbnail")
    private String video_thumbnail;

    @SerializedName("video_duration")
    private String video_duration;

    private String ser_no;

    private String title_cd;

    @SerializedName("_id")
    private String _id;

    @SerializedName("title_name")
    private String title_name;


    public Brief_CV() {

    }

    protected Brief_CV(Parcel in) {
        language_id = in.readParcelable(UserLangauges.class.getClassLoader());
        native_name = in.readString();
        brief_cv = in.readString();
        isChecked = in.readByte() != 0;
        english_name = in.readString();
        video_url = in.readString();
        video_thumbnail = in.readString();
        video_duration = in.readString();
        ser_no = in.readString();
        _id = in.readString();

        title_cd = in.readString();
        title_name = in.readString();
    }

    public static final Creator<Brief_CV> CREATOR = new Creator<Brief_CV>() {
        @Override
        public Brief_CV createFromParcel(Parcel in) {
            return new Brief_CV(in);
        }

        @Override
        public Brief_CV[] newArray(int size) {
            return new Brief_CV[size];
        }
    };

    public UserLangauges getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(UserLangauges language_id) {
        this.language_id = language_id;
    }

    public String getNative_name() {
        return native_name;
    }

    public void setNative_name(String native_name) {
        this.native_name = native_name;
    }

    public String getBrief_cv() {
        return brief_cv;
    }

    public void setBrief_cv(String brief_cv) {
        this.brief_cv = brief_cv;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getSer_no() {
        return ser_no;
    }

    public void setSer_no(String ser_no) {
        this.ser_no = ser_no;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getTitle_cd() {
        return title_cd;
    }

    public void setTitle_cd(String title_cd) {
        this.title_cd = title_cd;
    }


    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }


    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public static Creator<Brief_CV> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return native_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(language_id,i);
        parcel.writeString(native_name);
        parcel.writeString(brief_cv);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeString(english_name);
        parcel.writeString(video_url);
        parcel.writeString(video_thumbnail);
        parcel.writeString(video_duration);
        parcel.writeString(ser_no);
        parcel.writeString(title_cd);
        parcel.writeString(_id);
        parcel.writeString(title_name);

    }
}
