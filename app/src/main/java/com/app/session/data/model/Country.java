package com.app.session.data.model;

/**
 * Created by sourabh on 28/8/17.
 */

public class Country {
    private String CountryName, country_cd,dial_cd,country_icon,_id,currency_symbol;



    public Country() {

    }

    public String getCountry_cd() {
        return country_cd;
    }

    public void setCountry_cd(String country_cd) {
        this.country_cd = country_cd;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getDial_cd() {
        return dial_cd;
    }

    public void setDial_cd(String dial_cd) {
        this.dial_cd = dial_cd;
    }

    public String getCountry_icon() {
        return country_icon;
    }

    public void setCountry_icon(String country_icon) {
        this.country_icon = country_icon;
    }

    @Override
    public String toString() {
        return CountryName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }
}
