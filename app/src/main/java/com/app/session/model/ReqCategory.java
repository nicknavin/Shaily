
package com.app.session.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ReqCategory {

    @SerializedName("language_cd")
    private List<String> mLanguageCd;

    public List<String> getLanguageCd() {
        return mLanguageCd;
    }

    public void setLanguageCd(List<String> languageCd) {
        mLanguageCd = languageCd;
    }

}
