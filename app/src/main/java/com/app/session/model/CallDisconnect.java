package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CallDisconnect implements Parcelable
{
    private String callerID;
    private String reciverId;
    private String callingType;
    private String reciverName;

    public CallDisconnect()
    {}

    protected CallDisconnect(Parcel in) {
        callerID = in.readString();
        reciverId = in.readString();
        callingType = in.readString();
        reciverName = in.readString();
    }

    public static final Creator<CallDisconnect> CREATOR = new Creator<CallDisconnect>() {
        @Override
        public CallDisconnect createFromParcel(Parcel in) {
            return new CallDisconnect(in);
        }

        @Override
        public CallDisconnect[] newArray(int size) {
            return new CallDisconnect[size];
        }
    };

    public String getCallerID() {
        return callerID;
    }

    public void setCallerID(String callerID) {
        this.callerID = callerID;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getCallingType() {
        return callingType;
    }

    public void setCallingType(String callingType) {
        this.callingType = callingType;
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
        parcel.writeString(callerID);
        parcel.writeString(reciverId);
        parcel.writeString(callingType);
        parcel.writeString(reciverName);
    }
}
