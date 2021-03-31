package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddSubscription implements Parcelable {
    private String userId;
    private String languageId;
    private String categoryId;
    private String currencyId;
    private String subscriptionPrice;
    private String description;
    private String groupName;

    public AddSubscription()
    {

    }
    protected AddSubscription(Parcel in) {
        userId = in.readString();
        languageId = in.readString();
        categoryId = in.readString();
        currencyId = in.readString();
        subscriptionPrice = in.readString();
        description = in.readString();
        groupName = in.readString();
    }

    public static final Creator<AddSubscription> CREATOR = new Creator<AddSubscription>() {
        @Override
        public AddSubscription createFromParcel(Parcel in) {
            return new AddSubscription(in);
        }

        @Override
        public AddSubscription[] newArray(int size) {
            return new AddSubscription[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(String subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        parcel.writeString(userId);
        parcel.writeString(languageId);
        parcel.writeString(categoryId);
        parcel.writeString(currencyId);
        parcel.writeString(subscriptionPrice);
        parcel.writeString(description);
        parcel.writeString(groupName);
    }
}
