package com.app.session.model;

import com.google.gson.annotations.SerializedName;

public class CategoryId
{
    @SerializedName("_id")
    private String _id;

    @SerializedName("language_cd")
    private String language_cd;
    @SerializedName("category_name")
    private String category_name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
