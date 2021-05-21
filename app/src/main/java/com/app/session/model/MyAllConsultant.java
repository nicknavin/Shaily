
package com.app.session.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAllConsultant {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("category_cd")
    @Expose
    private Integer categoryCd;
    @SerializedName("subcategory_cd")
    @Expose
    private Integer subcategoryCd;
    @SerializedName("consultant_cd")
    @Expose
    private Integer consultantCd;
    @SerializedName("consultant_name")
    @Expose
    private String consultantName;
    @SerializedName("country_cd")
    @Expose
    private String countryCd;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("breaf_cv")
    @Expose
    private String breafCv;
    @SerializedName("cosultant")
    @Expose
    private String cosultant;
    @SerializedName("available")
    @Expose
    private Object available;
    @SerializedName("change_pwd_code")
    @Expose
    private Object changePwdCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(Integer categoryCd) {
        this.categoryCd = categoryCd;
    }

    public Integer getSubcategoryCd() {
        return subcategoryCd;
    }

    public void setSubcategoryCd(Integer subcategoryCd) {
        this.subcategoryCd = subcategoryCd;
    }

    public Integer getConsultantCd() {
        return consultantCd;
    }

    public void setConsultantCd(Integer consultantCd) {
        this.consultantCd = consultantCd;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getCountryCd() {
        return countryCd;
    }

    public void setCountryCd(String countryCd) {
        this.countryCd = countryCd;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBreafCv() {
        return breafCv;
    }

    public void setBreafCv(String breafCv) {
        this.breafCv = breafCv;
    }

    public String getCosultant() {
        return cosultant;
    }

    public void setCosultant(String cosultant) {
        this.cosultant = cosultant;
    }

    public Object getAvailable() {
        return available;
    }

    public void setAvailable(Object available) {
        this.available = available;
    }

    public Object getChangePwdCode() {
        return changePwdCode;
    }

    public void setChangePwdCode(Object changePwdCode) {
        this.changePwdCode = changePwdCode;
    }

}
