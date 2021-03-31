package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AudioVideoData implements Parcelable
{
    private String reciverId;
    private String userId;
    private String callerName;
    private String reciverName;
    private String ProfileUrl;
    private String reciever_profile_url;
    private String callType;
    private String agorachannelName;
    private String agoraTockenID;

    public AudioVideoData()
    {

    }

    protected AudioVideoData(Parcel in) {
        userId = in.readString();
        agorachannelName = in.readString();
        agoraTockenID = in.readString();
        reciverId = in.readString();
        callerName = in.readString();
        reciverName = in.readString();
        ProfileUrl = in.readString();
        reciever_profile_url = in.readString();
        callType = in.readString();
    }

    public static final Creator<AudioVideoData> CREATOR = new Creator<AudioVideoData>() {
        @Override
        public AudioVideoData createFromParcel(Parcel in) {
            return new AudioVideoData(in);
        }

        @Override
        public AudioVideoData[] newArray(int size) {
            return new AudioVideoData[size];
        }
    };

    public String getAgorachannelName() {
        return agorachannelName;
    }

    public void setAgorachannelName(String agorachannelName) {
        this.agorachannelName = agorachannelName;
    }

    public String getAgoraTockenID() {
        return agoraTockenID;
    }

    public void setAgoraTockenID(String agoraTockenID) {
        this.agoraTockenID = agoraTockenID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.ProfileUrl = profileUrl;
    }

    public String getReciever_profile_url() {
        return reciever_profile_url;
    }

    public void setReciever_profile_url(String reciever_profile_url) {
        this.reciever_profile_url = reciever_profile_url;
    }

    public static Creator<AudioVideoData> getCREATOR() {
        return CREATOR;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }


    public String getReciverName() {
        return reciverName;
    }

    public void setReciverName(String reciverName) {
        this.reciverName = reciverName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(agorachannelName);
        parcel.writeString(agoraTockenID);
        parcel.writeString(reciverId);
        parcel.writeString(callerName);
        parcel.writeString(reciverName);
        parcel.writeString(ProfileUrl);
        parcel.writeString(reciever_profile_url);
        parcel.writeString(callType);
    }
}
