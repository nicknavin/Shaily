package com.app.session.model;

/**
 * Created by sourabh on 23/8/17.
 */

public class Category {



    private String CategoryID;
    private String CategoryName;
    private String languageCd;
    private boolean isChecked;

    public Category(String categoryID, String categoryName, boolean isChecked) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        this.isChecked = isChecked;
    }
    public Category(String categoryID, String categoryName) {
        CategoryID = categoryID;
        CategoryName = categoryName;

    }
    public Category() {

    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        this.CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return CategoryName;
    }

    public String getLanguageCd() {
        return languageCd;
    }

    public void setLanguageCd(String languageCd) {
        this.languageCd = languageCd;
    }
}
