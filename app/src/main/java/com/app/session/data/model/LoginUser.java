package com.app.session.data.model;

import java.util.List;

public class LoginUser {
    private UserBank userBank;

    private String imageUrl;

    private List<UserLangauges> userLangauges;

//    private List<String> userCategory;

    private List<String> subscription_group_id;

    private String _id;

    private String email;

    private String login_user_id;

    private String mobile_no;

    private String user_password;

    private String user_name;

    private String country_id;

    private String gender;

//    private List<BriefCV> briefCV;

    private String is_consultant;

    private String status_id;

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

    public void setUserLangauges(List<UserLangauges> userLangauges) {
        this.userLangauges = userLangauges;
    }

    public List<UserLangauges> getUserLangauges() {
        return this.userLangauges;
    }

    //    public void setUserCategory(List<String> userCategory){
//        this.userCategory = userCategory;
//    }
//    public List<String> getUserCategory(){
//        return this.userCategory;
//    }
    public void setSubscription_group_id(List<String> subscription_group_id) {
        this.subscription_group_id = subscription_group_id;
    }

    public List<String> getSubscription_group_id() {
        return this.subscription_group_id;
    }

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

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_id() {
        return this.country_id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

//    public void setBriefCV(List<BriefCV> briefCV) {
//        this.briefCV = briefCV;
//    }
//
//    public List<BriefCV> getBriefCV() {
//        return this.briefCV;
//    }

    public void setIs_consultant(String is_consultant) {
        this.is_consultant = is_consultant;
    }

    public String getIs_consultant() {
        return this.is_consultant;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus_id() {
        return this.status_id;
    }

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


    @Override
    public String toString() {
        return "LoginUser{" +
                "userBank=" + userBank +
                ", imageUrl='" + imageUrl + '\'' +
                ", userLangauges=" + userLangauges +
                ", subscription_group_id=" + subscription_group_id +
                ", _id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", login_user_id='" + login_user_id + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_name='" + user_name + '\'' +
                ", country_id='" + country_id + '\'' +
                ", gender='" + gender + '\'' +
                ", is_consultant='" + is_consultant + '\'' +
                ", status_id='" + status_id + '\'' +
                ", mail_verfy='" + mail_verfy + '\'' +
                ", __v=" + __v +
                '}';
    }
}