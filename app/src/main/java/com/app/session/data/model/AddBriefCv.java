
package com.app.session.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AddBriefCv implements Parcelable {

    @SerializedName("brief_cv")
    private String mBriefCv;
    @SerializedName("language_id")
    private String mLanguageId;
    @SerializedName("user_id")
    private String mUserId;

    protected AddBriefCv(Parcel in) {
        mBriefCv = in.readString();
        mLanguageId = in.readString();
        mUserId = in.readString();
    }

    public AddBriefCv()
    {

    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBriefCv);
        dest.writeString(mLanguageId);
        dest.writeString(mUserId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddBriefCv> CREATOR = new Creator<AddBriefCv>() {
        @Override
        public AddBriefCv createFromParcel(Parcel in) {
            return new AddBriefCv(in);
        }

        @Override
        public AddBriefCv[] newArray(int size) {
            return new AddBriefCv[size];
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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
