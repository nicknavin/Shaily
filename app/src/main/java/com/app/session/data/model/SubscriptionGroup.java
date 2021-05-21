package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubscriptionGroup implements Parcelable
{
    private String category_cd;
    private String category_name;
    private String currency_cd;
    private String currency_name_en;

    private String group_desc;
    private String group_url_image_address;
    private String group_thumbnail_url;

    private String language_cd;
    private String language;
    private String subscription_group_cd;
    private String user_cd;

    @SerializedName("group_image_url")
    private String group_image_url;
    @SerializedName("group_introduction_video_url")
    private String group_introduction_video_url;
    @SerializedName("group_video_thumbnail_url")
    private String group_video_thumbnail_url;
    @SerializedName("_id")
    private String _id;
    @SerializedName("user_id")
    private UserDetails userDetails;
    @SerializedName("group_name")
    private String group_name;
    @SerializedName("language_id")
    private UserLangauges language_id;
    @SerializedName("category_id")
    private CategoryId category_id;
    @SerializedName("subscription_price")
    private double subscription_price;
    @SerializedName("currency_id")
    private CurrencyBody currency_id;
    @SerializedName("group_description")
    private String group_description;
    @SerializedName("__v")
    private int __v;


    public SubscriptionGroup()
    {

    }

    protected SubscriptionGroup(Parcel in) {
        category_cd = in.readString();
        category_name = in.readString();
        currency_cd = in.readString();
        currency_name_en = in.readString();
        group_desc = in.readString();
        group_introduction_video_url = in.readString();
        group_name = in.readString();
        group_url_image_address = in.readString();
        language_cd = in.readString();
        language = in.readString();
        subscription_price = in.readDouble();
        subscription_group_cd = in.readString();
        user_cd = in.readString();
        group_thumbnail_url = in.readString();
        userDetails = in.readParcelable(UserDetails.class.getClassLoader());
    }


    public static final Creator<SubscriptionGroup> CREATOR = new Creator<SubscriptionGroup>() {
        @Override
        public SubscriptionGroup createFromParcel(Parcel in) {
            return new SubscriptionGroup(in);
        }

        @Override
        public SubscriptionGroup[] newArray(int size) {
            return new SubscriptionGroup[size];
        }
    };

    public String getCategory_cd() {
        return category_cd;
    }

    public void setCategory_cd(String category_cd) {
        this.category_cd = category_cd;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCurrency_cd() {
        return currency_cd;
    }

    public void setCurrency_cd(String currency_cd) {
        this.currency_cd = currency_cd;
    }

    public String getCurrency_name_en() {
        return currency_name_en;
    }

    public void setCurrency_name_en(String currency_name_en) {
        this.currency_name_en = currency_name_en;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getGroup_introduction_video_url() {
        return group_introduction_video_url;
    }

    public void setGroup_introduction_video_url(String group_introduction_video_url) {
        this.group_introduction_video_url = group_introduction_video_url;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_url_image_address() {
        return group_url_image_address;
    }

    public void setGroup_url_image_address(String group_url_image_address) {
        this.group_url_image_address = group_url_image_address;
    }

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGroup_image_url() {
        return group_image_url;
    }

    public void setGroup_image_url(String group_image_url) {
        this.group_image_url = group_image_url;
    }

    public String getGroup_video_thumbnail_url() {
        return group_video_thumbnail_url;
    }

    public void setGroup_video_thumbnail_url(String group_video_thumbnail_url) {
        this.group_video_thumbnail_url = group_video_thumbnail_url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    public UserLangauges getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(UserLangauges language_id) {
        this.language_id = language_id;
    }

    public static Creator<SubscriptionGroup> getCREATOR() {
        return CREATOR;
    }

    public CategoryId getCategory_id() {
        return category_id;
    }

    public void setCategory_id(CategoryId category_id) {
        this.category_id = category_id;
    }

    public double getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(double subscription_price) {
        this.subscription_price = subscription_price;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getSubscription_group_cd() {
        return subscription_group_cd;
    }

    public void setSubscription_group_cd(String subscription_group_cd) {
        this.subscription_group_cd = subscription_group_cd;
    }

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getGroup_thumbnail_url() {
        return group_thumbnail_url;
    }

    public void setGroup_thumbnail_url(String group_thumbnail_url) {
        this.group_thumbnail_url = group_thumbnail_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category_cd);
        parcel.writeString(category_name);
        parcel.writeString(currency_cd);
        parcel.writeString(currency_name_en);
        parcel.writeString(group_desc);
        parcel.writeString(group_url_image_address);
        parcel.writeString(group_thumbnail_url);
        parcel.writeString(language_cd);
        parcel.writeString(language);
        parcel.writeString(subscription_group_cd);
        parcel.writeString(user_cd);
        parcel.writeString(group_image_url);
        parcel.writeString(group_introduction_video_url);
        parcel.writeString(group_video_thumbnail_url);
        parcel.writeString(_id);
        parcel.writeParcelable(userDetails,i);
        parcel.writeString(group_name);
        parcel.writeParcelable(language_id,i);
        parcel.writeDouble(subscription_price);
        parcel.writeParcelable(currency_id,i);
        parcel.writeString(group_description);
        parcel.writeInt(__v);
    }
}
