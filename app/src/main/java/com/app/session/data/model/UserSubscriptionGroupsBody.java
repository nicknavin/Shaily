package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class UserSubscriptionGroupsBody implements Parcelable
{
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


    @SerializedName("subscription_price")
    private String subscription_price;

    @SerializedName("group_description")
    private String group_description;

    @SerializedName("language_id")
    private UserLangauges language_id;

    @Nullable
    @SerializedName("category_id")
    private CategoryId category_id;


    @SerializedName("currency_id")
    private CurrencyBody currency_id;

    @SerializedName("user_id")
    private UserDetails userDetails;

    public UserSubscriptionGroupsBody()
    {

    }

    protected UserSubscriptionGroupsBody(Parcel in) {
        group_image_url = in.readString();
        group_introduction_video_url = in.readString();
        _id = in.readString();
        group_video_thumbnail_url = in.readString();
        group_name = in.readString();
        subscription_price = in.readString();
        group_description = in.readString();
        category_id=in.readParcelable(CategoryId.class.getClassLoader());
        language_id = in.readParcelable(UserLangauges.class.getClassLoader());
        currency_id = in.readParcelable(CurrencyBody.class.getClassLoader());
        userDetails = in.readParcelable(UserDetails.class.getClassLoader());
    }

    public static final Creator<UserSubscriptionGroupsBody> CREATOR = new Creator<UserSubscriptionGroupsBody>() {
        @Override
        public UserSubscriptionGroupsBody createFromParcel(Parcel in) {
            return new UserSubscriptionGroupsBody(in);
        }

        @Override
        public UserSubscriptionGroupsBody[] newArray(int size) {
            return new UserSubscriptionGroupsBody[size];
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

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
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

    public CurrencyBody getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(CurrencyBody currency_id) {
        this.currency_id = currency_id;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(group_image_url);
        parcel.writeString(group_introduction_video_url);
        parcel.writeString(_id);
        parcel.writeString(group_video_thumbnail_url);
        parcel.writeString(group_name);
        parcel.writeString(subscription_price);
        parcel.writeString(group_description);
        parcel.writeParcelable(category_id, i);
        parcel.writeParcelable(language_id, i);
        parcel.writeParcelable(currency_id, i);
        parcel.writeParcelable(userDetails, i);
    }


//    @Override
//    public String toString() {
//        return "UserSubscriptionGroupsBody{" +
//                "group_image_url='" + group_image_url + '\'' +
//                ", group_introduction_video_url='" + group_introduction_video_url + '\'' +
//                ", _id='" + _id + '\'' +
//                ", group_video_thumbnail_url='" + group_video_thumbnail_url + '\'' +
//                ", group_name='" + group_name + '\'' +
//                ", subscription_price='" + subscription_price + '\'' +
//                ", group_description='" + group_description + '\'' +
//                ", language_id=" + language_id.getName() +
//                ", category_id=" + category_id.toString() +
//                ", currency_id=" + currency_id .toString()+
//                ", userDetails=" + userDetails.toString() +
//                '}';
//    }
}
