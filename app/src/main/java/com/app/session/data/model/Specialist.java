package com.app.session.data.model;

/**
 * Created by ITEGRITYPRO on 09-06-2018.
 */

public class Specialist
{
  private String subcategory_cd;
  private String icon_address;
  private String profession;
  private String category_cd;
  private String country_icon;

    public String getSubcategory_cd() {
        return subcategory_cd;
    }

    public void setSubcategory_cd(String subcategory_cd) {
        this.subcategory_cd = subcategory_cd;
    }

    public String getIcon_address() {
        return icon_address;
    }

    public void setIcon_address(String icon_address) {
        this.icon_address = icon_address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCategory_cd() {
        return category_cd;
    }

    public void setCategory_cd(String category_cd) {
        this.category_cd = category_cd;
    }
}
