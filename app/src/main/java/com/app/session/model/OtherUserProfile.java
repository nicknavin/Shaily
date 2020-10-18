package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfile
{
    private UserBank userBank;

    private String imageUrl;

    @SerializedName("userLangauges")
    private ArrayList<UserLangauges> userLangauges;

    @SerializedName("userCategory")
    private ArrayList<CategoryBody> userCategory;

    @SerializedName("subscription_group_id")
    private ArrayList<SubscriptionGroupId> subscription_group_id;

    @SerializedName("_id")
    private String _id;

    @SerializedName("email")
    private String email;

    @SerializedName("login_user_id")
    private String login_user_id;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("user_password")
    private String user_password;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("country_id")
    private CountryId country_id;

    @SerializedName("gender")
    private String gender;

    @SerializedName("briefCV")
    private ArrayList<BriefCV> briefCV;

    @SerializedName("is_consultant")
    private String is_consultant;

//    private String status_id;

    private String mail_verfy;

    private int __v;

    public void setUserBank(UserBank userBank) {
        this.userBank = userBank;
    }

    public UserBank getUserBank() {
        return this.userBank;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }



    public ArrayList<UserLangauges> getUserLangauges() {
        return this.userLangauges;
    }

    public ArrayList<CategoryBody> getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(ArrayList<CategoryBody> userCategory) {
        this.userCategory = userCategory;
    }

//    public void setSubscription_group_id(List<String> subscription_group_id) {
//        this.subscription_group_id = subs`cription_group_id;
//    }
//
//    public List<String> getSubscription_group_id() {
//        return this.subscription_group_id;
//    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }

    public String getLogin_user_id() {
        return this.login_user_id;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMobile_no() {
        return this.mobile_no;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_password() {
        return this.user_password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return this.user_name;
    }



    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

//    public List<SubscriptionGroupId> getSubscription_group_id() {
//        return subscription_group_id;
//    }
//
//    public void setSubscription_group_id(List<SubscriptionGroupId> subscription_group_id) {
//        this.subscription_group_id = subscription_group_id;
//    }

    public ArrayList<BriefCV> getBriefCV() {
        return briefCV;
    }

    public void setBriefCV(ArrayList<BriefCV> briefCV) {
        this.briefCV = briefCV;
    }

    public void setIs_consultant(String is_consultant) {
        this.is_consultant = is_consultant;
    }

    public String getIs_consultant() {
        return this.is_consultant;
    }

//    public void setStatus_id(String status_id) {
//        this.status_id = status_id;
//    }
//
//    public String getStatus_id() {
//        return this.status_id;
//    }

    public void setMail_verfy(String mail_verfy) {
        this.mail_verfy = mail_verfy;
    }

    public String getMail_verfy() {
        return this.mail_verfy;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public int get__v() {
        return this.__v;
    }

    public CountryId getCountry_id() {
        return country_id;
    }

    public void setCountry_id(CountryId country_id) {
        this.country_id = country_id;
    }

    public void setUserLangauges(ArrayList<UserLangauges> userLangauges) {
        this.userLangauges = userLangauges;
    }

    public ArrayList<SubscriptionGroupId> getSubscription_group_id() {
        return subscription_group_id;
    }

    public void setSubscription_group_id(ArrayList<SubscriptionGroupId> subscription_group_id) {
        this.subscription_group_id = subscription_group_id;
    }
}
