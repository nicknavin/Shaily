package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class ChatedBody
{
    @SerializedName("body")
    ChatedPersonsBody chatedPersonsBody;
    @SerializedName("notification")
    private String notification;
    public ChatedPersonsBody getChatedPersonsBody() {
        return chatedPersonsBody;
    }

    public void setChatedPersonsBody(ChatedPersonsBody chatedPersonsBody) {
        this.chatedPersonsBody = chatedPersonsBody;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
