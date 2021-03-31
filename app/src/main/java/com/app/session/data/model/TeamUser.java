package com.app.session.data.model;

/**
 * Created by ITEGRITYPRO on 07-04-2018.
 */

public class TeamUser
{
    private String user_cd;
    private String user_name;
    private String language;
    private String email_address;
    private String category_name_english;
    private String is_consultant;
    private String imageUrl;
    private String invitition;
    private String company_cd;
    private String login_user_id;

    public String getLogin_user_id() {
        return login_user_id;
    }

    public void setLogin_user_id(String login_user_id) {
        this.login_user_id = login_user_id;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getCategory_name_english() {
        return category_name_english;
    }

    public void setCategory_name_english(String category_name_english) {
        this.category_name_english = category_name_english;
    }

    public String getIs_consultant() {
        return is_consultant;
    }

    public void setIs_consultant(String is_consultant) {
        this.is_consultant = is_consultant;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInvitition() {
        return invitition;
    }

    public void setInvitition(String invitition) {
        this.invitition = invitition;
    }

    public String getCompany_cd() {
        return company_cd;
    }

    public void setCompany_cd(String company_cd) {
        this.company_cd = company_cd;
    }
}
