
package com.app.session.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalUserStory {

    @SerializedName ("load")
    private String load;
    @SerializedName("person_user_id")
    private String personUserId;


    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getPersonUserId() {
        return personUserId;
    }

    public void setPersonUserId(String personUserId) {
        this.personUserId = personUserId;
    }

}
