package com.app.session.model;

/**
 * Created by sourabh on 28/8/17.
 */

public class SubCategory
{

    private String category_cd,subcategory_cd,subCategoryName;
    private String category_name_arabic, category_name_english;

    public SubCategory(String category_cd, String subcategory_cd, String category_name_arabic, String category_name_english, String subCategoryName) {
        this.category_cd = category_cd;
        this.subcategory_cd = subcategory_cd;
        this.category_name_arabic = category_name_arabic;
        this.category_name_english = category_name_english;
        this.subCategoryName=subCategoryName;
    }
    public SubCategory()
    {

    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public void setCategory_name_arabic(String category_name_arabic) {
        this.category_name_arabic = category_name_arabic;
    }

    public void setCategory_name_english(String category_name_english) {
        this.category_name_english = category_name_english;
    }



    public String getCategory_name_arabic() {
        return category_name_arabic;
    }

    public String getCategory_name_english() {
        return category_name_english;
    }


    public String getCategory_cd() {
        return category_cd;
    }

    public void setCategory_cd(String category_cd) {
        this.category_cd = category_cd;
    }

    public String getSubcategory_cd() {
        return subcategory_cd;
    }

    public void setSubcategory_cd(String subcategory_cd) {
        this.subcategory_cd = subcategory_cd;
    }
}
