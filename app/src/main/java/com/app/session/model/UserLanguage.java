package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserLanguage implements Parcelable
{
    private String language_cd;

    private String english_name;

    private String native_name;

    private int ser_no;

    private String breaf_cv;

    public UserLanguage()
    {

    }
    protected UserLanguage(Parcel in) {
        language_cd = in.readString();
        english_name = in.readString();
        native_name = in.readString();
        ser_no = in.readInt();
        breaf_cv = in.readString();
    }

    public static final Creator<UserLanguage> CREATOR = new Creator<UserLanguage>() {
        @Override
        public UserLanguage createFromParcel(Parcel in) {
            return new UserLanguage(in);
        }

        @Override
        public UserLanguage[] newArray(int size) {
            return new UserLanguage[size];
        }
    };

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getNative_name() {
        return native_name;
    }

    public void setNative_name(String native_name) {
        this.native_name = native_name;
    }

    public int getSer_no() {
        return ser_no;
    }

    public void setSer_no(int ser_no) {
        this.ser_no = ser_no;
    }

    public String getBreaf_cv() {
        return breaf_cv;
    }

    public void setBreaf_cv(String breaf_cv) {
        this.breaf_cv = breaf_cv;
    }
    @Override
    public String toString() {
        return native_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(language_cd);
        parcel.writeString(english_name);
        parcel.writeString(native_name);
        parcel.writeInt(ser_no);
        parcel.writeString(breaf_cv);
    }
}
