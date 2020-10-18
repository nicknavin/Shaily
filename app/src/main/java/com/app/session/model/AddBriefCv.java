
package com.app.session.model;


import com.google.gson.annotations.SerializedName;


public class AddBriefCv {

    @SerializedName("brief_cv")
    private String mBriefCv;
    @SerializedName("language_id")
    private String mLanguageId;
    @SerializedName("user_id")
    private String mUserId;

    public String getBriefCv() {
        return mBriefCv;
    }

    public void setBriefCv(String briefCv) {
        mBriefCv = briefCv;
    }

    public String getLanguageId() {
        return mLanguageId;
    }

    public void setLanguageId(String languageId) {
        mLanguageId = languageId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
