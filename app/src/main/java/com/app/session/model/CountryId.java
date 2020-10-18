package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class CountryId {

    @SerializedName("_id")
    private String _id;
    @SerializedName("country_arabic")
    private String country_arabic;
    @SerializedName("country_cd")
    private String country_cd;
    @SerializedName("country_english")
    private String country_english;
    @SerializedName("currency_symbol")
    private String currency_symbol;
    @SerializedName("dial_cd")
    private String dial_cd;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCountry_arabic() {
        return country_arabic;
    }

    public void setCountry_arabic(String country_arabic) {
        this.country_arabic = country_arabic;
    }

    public String getCountry_cd() {
        return country_cd;
    }

    public void setCountry_cd(String country_cd) {
        this.country_cd = country_cd;
    }

    public String getCountry_english() {
        return country_english;
    }

    public void setCountry_english(String country_english) {
        this.country_english = country_english;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getDial_cd() {
        return dial_cd;
    }

    public void setDial_cd(String dial_cd) {
        this.dial_cd = dial_cd;
    }
}
