package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserLangauges implements Parcelable
{
    @SerializedName("_id")
    private String _id;
    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;
    @SerializedName("nativeName")
    private String nativeName;

    private boolean isChecked;


    protected UserLangauges(Parcel in) {
        _id = in.readString();
        code = in.readString();
        name = in.readString();
        nativeName = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<UserLangauges> CREATOR = new Creator<UserLangauges>() {
        @Override
        public UserLangauges createFromParcel(Parcel in) {
            return new UserLangauges(in);
        }

        @Override
        public UserLangauges[] newArray(int size) {
            return new UserLangauges[size];
        }
    };

    public void set_id(String _id){
        this._id = _id;
    }
    public String get_id(){
        return this._id;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setNativeName(String nativeName){
        this.nativeName = nativeName;
    }
    public String getNativeName(){
        return this.nativeName;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(code);
        parcel.writeString(name);
        parcel.writeString(nativeName);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public String toString() {
        return name;
    }


}