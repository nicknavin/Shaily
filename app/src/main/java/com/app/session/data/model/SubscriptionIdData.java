package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubscriptionIdData implements Parcelable
{
    public SubscriptionIdData() {

    }

    @SerializedName("group_image_url")
    private String group_image_url;

    @SerializedName("group_introduction_video_url")
    private String group_introduction_video_url;

    @SerializedName("_id")
    private String _id;

    @SerializedName("group_video_thumbnail_url")
    private String group_video_thumbnail_url;

    @SerializedName("group_name")
    private String group_name;

    @SerializedName("language_id")
    private UserLangauges language_id;

    @SerializedName("category_id")
    private CategoryId category_id;

    @SerializedName("subscription_price")
    private String subscription_price;

    @SerializedName("currency_id")
    private CurrencyBody currency_id;

    @SerializedName("group_description")
    private String group_description;


    @SerializedName("user_id")
    private UserDetails userDetails;


    protected SubscriptionIdData(Parcel in) {
        group_image_url = in.readString();
        group_introduction_video_url = in.readString();
        _id = in.readString();
        group_video_thumbnail_url = in.readString();
        group_name = in.readString();
        language_id = in.readParcelable(UserLangauges.class.getClassLoader());
        category_id = in.readParcelable(CategoryId.class.getClassLoader());
        subscription_price = in.readString();
        currency_id = in.readParcelable(CurrencyBody.class.getClassLoader());
        group_description = in.readString();
        userDetails = in.readParcelable(UserDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_image_url);
        dest.writeString(group_introduction_video_url);
        dest.writeString(_id);
        dest.writeString(group_video_thumbnail_url);
        dest.writeString(group_name);
        dest.writeParcelable(language_id, flags);
        dest.writeParcelable(category_id, flags);
        dest.writeString(subscription_price);
        dest.writeParcelable(currency_id, flags);
        dest.writeString(group_description);
        dest.writeParcelable(userDetails, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscriptionIdData> CREATOR = new Creator<SubscriptionIdData>() {
        @Override
        public SubscriptionIdData createFromParcel(Parcel in) {
            return new SubscriptionIdData(in);
        }

        @Override
        public SubscriptionIdData[] newArray(int size) {
            return new SubscriptionIdData[size];
        }
    };

    public String getGroup_image_url() {
        return group_image_url;
    }

    public void setGroup_image_url(String group_image_url) {
        this.group_image_url = group_image_url;
    }

    public String getGroup_introduction_video_url() {
        return group_introduction_video_url;
    }

    public void setGroup_introduction_video_url(String group_introduction_video_url) {
        this.group_introduction_video_url = group_introduction_video_url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroup_video_thumbnail_url() {
        return group_video_thumbnail_url;
    }

    public void setGroup_video_thumbnail_url(String group_video_thumbnail_url) {
        this.group_video_thumbnail_url = group_video_thumbnail_url;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public UserLangauges getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(UserLangauges language_id) {
        this.language_id = language_id;
    }

    public CategoryId getCategory_id() {
        return category_id;
    }

    public void setCategory_id(CategoryId category_id) {
        this.category_id = category_id;
    }

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }

    public CurrencyBody getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(CurrencyBody currency_id) {
        this.currency_id = currency_id;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
