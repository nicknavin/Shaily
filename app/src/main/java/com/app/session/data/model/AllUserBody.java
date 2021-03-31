
package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllUserBody {



    @SerializedName("user_name")
    private String userName;

    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("login_user_id")
    private String loginUserId;

    @SerializedName("_id")
    private String _id;

    @SerializedName("briefCV")
    private List<BriefCVUser> briefCV;

    @SerializedName("country_id")
    private CountryId countryId;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<BriefCVUser> getBriefCV() {
        return briefCV;
    }

    public void setBriefCV(List<BriefCVUser> briefCV) {
        this.briefCV = briefCV;
    }

    public CountryId getCountryId() {
        return countryId;
    }

    public void setCountryId(CountryId countryId) {
        this.countryId = countryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
