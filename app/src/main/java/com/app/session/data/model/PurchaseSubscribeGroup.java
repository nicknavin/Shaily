package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PurchaseSubscribeGroup implements Parcelable
{

    @SerializedName("story_to_read")
    private int story_to_read;

    @SerializedName("_id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("subscription_id")
    private SubscriptionIdData subscriptionIdData;

    protected PurchaseSubscribeGroup(Parcel in) {
        story_to_read = in.readInt();
        id = in.readString();
        userId = in.readString();
        subscriptionIdData = in.readParcelable(SubscriptionIdData.class.getClassLoader());
    }

    public static final Creator<PurchaseSubscribeGroup> CREATOR = new Creator<PurchaseSubscribeGroup>() {
        @Override
        public PurchaseSubscribeGroup createFromParcel(Parcel in) {
            return new PurchaseSubscribeGroup(in);
        }

        @Override
        public PurchaseSubscribeGroup[] newArray(int size) {
            return new PurchaseSubscribeGroup[size];
        }
    };

    public int getStory_to_read() {
        return story_to_read;
    }

    public void setStory_to_read(int story_to_read) {
        this.story_to_read = story_to_read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SubscriptionIdData getSubscriptionIdData() {
        return subscriptionIdData;
    }

    public void setSubscriptionIdData(SubscriptionIdData subscriptionIdData) {
        this.subscriptionIdData = subscriptionIdData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(story_to_read);
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeParcelable(subscriptionIdData, i);
    }
}
