package com.app.session.data.model;

/**
 * Created by ITEGRITYPRO on 25-04-2018.
 */

public class NotificationModule
{
    private String type;

    private String from_user_cd;

    private String user_cd;

    private String request;

    private String from_user_name;

    private String Message;

    private String imageUrl;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom_user_cd() {
        return from_user_cd;
    }

    public void setFrom_user_cd(String from_user_cd) {
        this.from_user_cd = from_user_cd;
    }

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
