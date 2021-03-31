package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class OtherUserId
{
    @SerializedName("person_user_id")
    private String person_user_id;

    public String getPerson_user_id() {
        return person_user_id;
    }

    public void setPerson_user_id(String person_user_id) {
        this.person_user_id = person_user_id;
    }
}
