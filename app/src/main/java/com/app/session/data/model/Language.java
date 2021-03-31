package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ITEGRITYPRO on 05-02-2018.
 */

public class Language
{


    private String language_cd;
    private String english_name;
    private String native_name;
    private boolean preferred_lang;
    private boolean isChecked;

    @SerializedName("_id")
    private String _id;
    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;
    @SerializedName("nativeName")
    private String nativeName;





    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getNative_name() {
        return native_name;
    }

    public void setNative_name(String native_name) {
        this.native_name = native_name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isPreferred_lang() {
        return preferred_lang;
    }

    public void setPreferred_lang(boolean preferred_lang) {
        this.preferred_lang = preferred_lang;
    }

    @Override
    public String toString() {
        return native_name;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

}
