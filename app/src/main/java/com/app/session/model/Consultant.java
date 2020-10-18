package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sourabh on 28/8/17.
 */

public class Consultant implements Parcelable
{

    private String user_cd;

    private String email_address;

    private String mobile_no;

    private String user_name;

    private String country_cd;

    private String change_pwd_code_link;

    private String imageUrl;

    private String gender;

    private String is_consultant;

    private String login_user_id;


    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
    }

    public static Creator<Consultant> getCREATOR() {
        return CREATOR;
    }

    protected Consultant(Parcel in) {
        user_cd = in.readString();
        email_address = in.readString();
        mobile_no = in.readString();
        user_name = in.readString();
        country_cd = in.readString();
        change_pwd_code_link = in.readString();
        imageUrl = in.readString();
        gender = in.readString();
        is_consultant = in.readString();
        login_user_id = in.readString();
    }
    public Consultant()
    {

    }

    public static final Creator<Consultant> CREATOR = new Creator<Consultant>() {
        @Override
        public Consultant createFromParcel(Parcel in) {
            return new Consultant(in);
        }

        @Override
        public Consultant[] newArray(int size) {
            return new Consultant[size];
        }
    };

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public void setEmail_address(String email_address){
        this.email_address = email_address;
    }
    public String getEmail_address(){
        return this.email_address;
    }
    public void setMobile_no(String mobile_no){
        this.mobile_no = mobile_no;
    }
    public String getMobile_no(){
        return this.mobile_no;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public String getUser_name(){
        return this.user_name;
    }
    public void setCountry_cd(String country_cd){
        this.country_cd = country_cd;
    }
    public String getCountry_cd(){
        return this.country_cd;
    }
    public void setChange_pwd_code_link(String change_pwd_code_link){
        this.change_pwd_code_link = change_pwd_code_link;
    }
    public String getChange_pwd_code_link(){
        return this.change_pwd_code_link;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }
    public void setIs_consultant(String is_consultant){
        this.is_consultant = is_consultant;
    }
    public String getIs_consultant(){
        return this.is_consultant;
    }

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
        dest.writeString(login_user_id);
    }
}
