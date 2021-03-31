package com.app.session.data.model;

public class CountrySpinner
{
    private String CountryName, country_cd,dial_cd,country_icon;

    public CountrySpinner(String cntry_name, String cntry_cd, String dial_cd, String country_icon) {
        this.CountryName = cntry_name;
        this.country_cd = cntry_cd;
        this.dial_cd=dial_cd;
        this.country_icon=country_icon;
    }

    public CountrySpinner() {

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

}
