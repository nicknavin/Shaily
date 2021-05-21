
package com.app.session.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SubscriptionId implements Parcelable {

    @SerializedName("_id")
    private String _id;
    @SerializedName("group_image_url")
    private String groupImageUrl;
    @SerializedName("group_name")
    private String groupName;


   public SubscriptionId()
    {

    }


    protected SubscriptionId(Parcel in) {
        _id = in.readString();
        groupImageUrl = in.readString();
        groupName = in.readString();
    }

    public static final Creator<SubscriptionId> CREATOR = new Creator<SubscriptionId>() {
        @Override
        public SubscriptionId createFromParcel(Parcel in) {
            return new SubscriptionId(in);
        }

        @Override
        public SubscriptionId[] newArray(int size) {
            return new SubscriptionId[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(groupImageUrl);
        parcel.writeString(groupName);
    }
}
