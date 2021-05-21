package com.app.session.model;

import com.app.session.section.Section;

import java.util.List;

public class LanguageCategoryData implements Section<Category> {
    private String language_cd;
    private String native_name;
    private List<Category> categoryList;

    public String getLanguage_cd() {
        return language_cd;
    }

    public void setLanguage_cd(String language_cd) {
        this.language_cd = language_cd;
    }

    public String getNative_name() {
        return native_name;
    }

    public void setNative_name(String native_name) {
        this.native_name = native_name;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public List<Category> getChildItems() {
        return categoryList;
    }
}
