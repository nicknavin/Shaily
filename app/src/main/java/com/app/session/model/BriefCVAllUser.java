
package com.app.session.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class BriefCVAllUser implements Parcelable {

    @SerializedName("brief_cv")
    private String mBriefCv;
    @SerializedName("language_id")
    private String mLanguageId;
    @SerializedName("title_name")
    private String mTitleName;
    @SerializedName("video_duration")
    private String mVideoDuration;
    @SerializedName("video_thumbnail")
    private String mVideoThumbnail;
    @SerializedName("video_url")
    private String mVideoUrl;
    @SerializedName("_id")
    private String m_id;

    protected BriefCVAllUser(Parcel in) {
        mBriefCv = in.readString();
        mLanguageId = in.readString();
        mTitleName = in.readString();
        mVideoDuration = in.readString();
        mVideoThumbnail = in.readString();
        mVideoUrl = in.readString();
        m_id = in.readString();
    }

    public static final Creator<BriefCVAllUser> CREATOR = new Creator<BriefCVAllUser>() {
        @Override
        public BriefCVAllUser createFromParcel(Parcel in) {
            return new BriefCVAllUser(in);
        }

        @Override
        public BriefCVAllUser[] newArray(int size) {
            return new BriefCVAllUser[size];
        }
    };

    public String getBriefCv() {
        return mBriefCv;
    }

    public void setBriefCv(String briefCv) {
        mBriefCv = briefCv;
    }

    public String getLanguageId() {
        return mLanguageId;
    }

    public void setLanguageId(String languageId) {
        mLanguageId = languageId;
    }

    public String getTitleName() {
        return mTitleName;
    }

    public void setTitleName(String titleName) {
        mTitleName = titleName;
    }

    public String getVideoDuration() {
        return mVideoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        mVideoDuration = videoDuration;
    }

    public String getVideoThumbnail() {
        return mVideoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        mVideoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String get_id() {
        return m_id;
    }

    public void set_id(String _id) {
        m_id = _id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mBriefCv);
        parcel.writeString(mLanguageId);
        parcel.writeString(mTitleName);
        parcel.writeString(mVideoDuration);
        parcel.writeString(mVideoThumbnail);
        parcel.writeString(mVideoUrl);
        parcel.writeString(m_id);
    }
}
