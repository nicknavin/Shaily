package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubscriptionGroupBody implements Parcelable
{
    @SerializedName("group_image_url")
    private String group_image_url;
    @SerializedName("group_introduction_video_url")
    private String group_introduction_video_url;
    @SerializedName("group_video_thumbnail_url")
    private String group_video_thumbnail_url;
    @SerializedName("_id")
    private String _id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("group_name")
    private String group_name;
    @SerializedName("language_id")
    private String language_id;
    @SerializedName("category_id")
    private CategoryId category_id;
    @SerializedName("subscription_price")
    private double subscription_price;
    @SerializedName("currency_id")
    private String currency_id;
    @SerializedName("group_description")
    private String group_description;
    @SerializedName("__v")
    private int __v;


    protected SubscriptionGroupBody(Parcel in) {
        group_image_url = in.readString();
        group_introduction_video_url = in.readString();
        group_video_thumbnail_url = in.readString();
        _id = in.readString();
        user_id = in.readString();
        group_name = in.readString();
        language_id = in.readString();
        subscription_price = in.readDouble();
        currency_id = in.readString();
        group_description = in.readString();
        __v = in.readInt();
    }

    public static final Creator<SubscriptionGroupBody> CREATOR = new Creator<SubscriptionGroupBody>() {
        @Override
        public SubscriptionGroupBody createFromParcel(Parcel in) {
            return new SubscriptionGroupBody(in);
        }

        @Override
        public SubscriptionGroupBody[] newArray(int size) {
            return new SubscriptionGroupBody[size];
        }
    };

    public void setGroup_image_url(String group_image_url){
        this.group_image_url = group_image_url;
    }
    public String getGroup_image_url(){
        return this.group_image_url;
    }
    public void setGroup_introduction_video_url(String group_introduction_video_url){
        this.group_introduction_video_url = group_introduction_video_url;
    }
    public String getGroup_introduction_video_url(){
        return this.group_introduction_video_url;
    }
    public void setGroup_video_thumbnail_url(String group_video_thumbnail_url){
        this.group_video_thumbnail_url = group_video_thumbnail_url;
    }
    public String getGroup_video_thumbnail_url(){
        return this.group_video_thumbnail_url;
    }
    public void set_id(String _id){
        this._id = _id;
    }
    public String get_id(){
        return this._id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public String getUser_id(){
        return this.user_id;
    }
    public void setGroup_name(String group_name){
        this.group_name = group_name;
    }
    public String getGroup_name(){
        return this.group_name;
    }
    public void setLanguage_id(String language_id){
        this.language_id = language_id;
    }
    public String getLanguage_id(){
        return this.language_id;
    }

    public CategoryId getCategory_id() {
        return category_id;
    }

    public void setCategory_id(CategoryId category_id) {
        this.category_id = category_id;
    }

    public void setSubscription_price(double subscription_price){
        this.subscription_price = subscription_price;
    }
    public double getSubscription_price(){
        return this.subscription_price;
    }
    public void setCurrency_id(String currency_id){
        this.currency_id = currency_id;
    }
    public String getCurrency_id(){
        return this.currency_id;
    }
    public void setGroup_description(String group_description){
        this.group_description = group_description;
    }
    public String getGroup_description(){
        return this.group_description;
    }
    public void set__v(int __v){
        this.__v = __v;
    }
    public int get__v(){
        return this.__v;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(group_image_url);
        parcel.writeString(group_introduction_video_url);
        parcel.writeString(group_video_thumbnail_url);
        parcel.writeString(_id);
        parcel.writeString(user_id);
        parcel.writeString(group_name);
        parcel.writeString(language_id);
        parcel.writeDouble(subscription_price);
        parcel.writeString(currency_id);
        parcel.writeString(group_description);
        parcel.writeInt(__v);
    }
}
