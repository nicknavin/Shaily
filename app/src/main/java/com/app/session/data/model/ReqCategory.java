
package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


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
