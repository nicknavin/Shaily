package com.app.session.data.model;

/**
 * Created by ITEGRITYPRO on 07-02-2018.
 */

public class SubCategoryModel
{
    private String subcategory_cd;
    private String subCategoryName;
    private boolean isChecked;

    public String getSubcategory_cd() {
        return subcategory_cd;
    }

    public void setSubcategory_cd(String subcategory_cd) {
        this.subcategory_cd = subcategory_cd;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
