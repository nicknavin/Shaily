package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class CurrencyRef
{

    @SerializedName("country_cd")
    private String country_cd;

    @SerializedName("currency_cd")
    private String currency_cd;

    @SerializedName("currency_name_en")
    private String currency_name_en;

    @SerializedName("currency_name_local")
    private String currency_name_local;

    public String getCountry_cd() {
        return country_cd;
    }

    public void setCountry_cd(String country_cd) {
        this.country_cd = country_cd;
    }

    public String getCurrency_cd() {
        return currency_cd;
    }

    public void setCurrency_cd(String currency_cd) {
        this.currency_cd = currency_cd;
    }

    public String getCurrency_name_en() {
        return currency_name_en;
    }

    public void setCurrency_name_en(String currency_name_en) {
        this.currency_name_en = currency_name_en;
    }

    public String getCurrency_name_local() {
        return currency_name_local;
    }

    public void setCurrency_name_local(String currency_name_local) {
        this.currency_name_local = currency_name_local;
    }

    @NonNull
    @Override
    public String toString() {
        return currency_name_en;
    }
}
