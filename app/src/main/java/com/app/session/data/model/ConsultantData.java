package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsultantData implements Parcelable {

    private String category_cd;

    private String category_name;

    private String country_cd;

    private String country_icon;

    private String cv;

    private String imageUrl;

    private String imgUrl;

    private String is_company;

    private String language_cd;

    private String login_user_id;

    private String profession_code;

    private String status_cd;

    private String status_name;

    private String user_cd;

    private String user_name;

    private String video_thumbnail;

    private String video_url;

    protected ConsultantData(Parcel in) {
        category_cd = in.readString();
        category_name = in.readString();
        country_cd = in.readString();
        country_icon = in.readString();
        cv = in.readString();
        imageUrl = in.readString();
        imgUrl = in.readString();
        is_company = in.readString();
        language_cd = in.readString();
        login_user_id = in.readString();
        profession_code = in.readString();
        status_cd = in.readString();
        status_name = in.readString();
        user_cd = in.readString();
        user_name = in.readString();
        video_thumbnail = in.readString();
        video_url = in.readString();
    }

    public static final Creator<ConsultantData> CREATOR = new Creator<ConsultantData>() {
        @Override
        public ConsultantData createFromParcel(Parcel in) {
            return new ConsultantData(in);
        }

        @Override
        public ConsultantData[] newArray(int size) {
            return new ConsultantData[size];
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

    public String getCountry_cd() {
        return country_cd;
    }

    public void setCountry_cd(String country_cd) {
        this.country_cd = country_cd;
    }

    public String getCountry_icon() {
        return country_icon;
    }

    public void setCountry_icon(String country_icon) {
        this.country_icon = country_icon;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIs_company() {
        return is_company;
    }

    public void setIs_company(String is_company) {
        this.is_company = is_company;
    }

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }

    public String getProfession_code() {
        return profession_code;
    }

    public void setProfession_code(String profession_code) {
        this.profession_code = profession_code;
    }

    public String getStatus_cd() {
        return status_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category_cd);
        parcel.writeString(category_name);
        parcel.writeString(country_cd);
        parcel.writeString(country_icon);
        parcel.writeString(cv);
        parcel.writeString(imageUrl);
        parcel.writeString(imgUrl);
        parcel.writeString(is_company);
        parcel.writeString(language_cd);
        parcel.writeString(login_user_id);
        parcel.writeString(profession_code);
        parcel.writeString(status_cd);
        parcel.writeString(status_name);
        parcel.writeString(user_cd);
        parcel.writeString(user_name);
        parcel.writeString(video_thumbnail);
        parcel.writeString(video_url);
    }
}
