package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class ChatedBody
{
    @SerializedName("body")
    ChatedPersonsBody chatedPersonsBody;

    public ChatedPersonsBody getChatedPersonsBody() {
        return chatedPersonsBody;
    }

    public void setChatedPersonsBody(ChatedPersonsBody chatedPersonsBody) {
        this.chatedPersonsBody = chatedPersonsBody;
    }
}
