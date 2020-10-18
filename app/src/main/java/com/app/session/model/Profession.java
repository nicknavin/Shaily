package com.app.session.model;

import java.util.ArrayList;

/**
 * Created by ITEGRITYPRO on 09-06-2018.
 */

public class Profession {
   private String category;
   private Specialist specialist;
   private ArrayList<Specialist> special_List =new ArrayList<>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public ArrayList<Specialist> getSpecial_List() {
        return special_List;
    }

    public void setSpecial_List(ArrayList<Specialist> special_List) {
        this.special_List = special_List;
    }
}
