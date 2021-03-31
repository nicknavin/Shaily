package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourabh on 28/8/17.
 */

public class SearchConsultant implements Parcelable {

    private String user_cd;

    private String email_address;

    private String mobile_no;

    private String user_name;

    private String country_cd;

    private String change_pwd_code_link;

    private String imageUrl;

    private String gender;

    private String is_consultant;
    private String category;
    private String category_cd;
    private String sub_category;
    private String sub_category_cd;
    private String language_name;
    private String language_cd;


    protected SearchConsultant(Parcel in) {
        user_cd = in.readString();
        email_address = in.readString();
        mobile_no = in.readString();
        user_name = in.readString();
        country_cd = in.readString();
        change_pwd_code_link = in.readString();
        imageUrl = in.readString();
        gender = in.readString();
        is_consultant = in.readString();
        category = in.readString();
        category_cd = in.readString();
        sub_category = in.readString();
        sub_category_cd = in.readString();
        language_name = in.readString();
        language_cd = in.readString();
    }

    public SearchConsultant()
    {

    }
    public static final Creator<SearchConsultant> CREATOR = new Creator<SearchConsultant>() {
        @Override
        public SearchConsultant createFromParcel(Parcel in) {
            return new SearchConsultant(in);
        }

        @Override
        public SearchConsultant[] newArray(int size) {
            return new SearchConsultant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_cd);
        dest.writeString(email_address);
        dest.writeString(mobile_no);
        dest.writeString(user_name);
        dest.writeString(country_cd);
        dest.writeString(change_pwd_code_link);
        dest.writeString(imageUrl);
        dest.writeString(gender);
        dest.writeString(is_consultant);
        dest.writeString(category);
        dest.writeString(category_cd);
        dest.writeString(sub_category);
        dest.writeString(sub_category_cd);
        dest.writeString(language_name);
        dest.writeString(language_cd);
    }


    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCountry_cd() {
        return country_cd;
    }

    public void setCountry_cd(String country_cd) {
        this.country_cd = country_cd;
    }

    public String getChange_pwd_code_link() {
        return change_pwd_code_link;
    }

    public void setChange_pwd_code_link(String change_pwd_code_link) {
        this.change_pwd_code_link = change_pwd_code_link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_consultant() {
        return is_consultant;
    }

    public void setIs_consultant(String is_consultant) {
        this.is_consultant = is_consultant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_cd() {
        return category_cd;
    }

    public void setCategory_cd(String category_cd) {
        this.category_cd = category_cd;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getSub_category_cd() {
        return sub_category_cd;
    }

    public void setSub_category_cd(String sub_category_cd) {
        this.sub_category_cd = sub_category_cd;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }
}
