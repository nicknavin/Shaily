
package com.app.session.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LanguageId implements Parcelable {

    @SerializedName("_id")
    private String _id;
    @SerializedName("name")
    private String name;
    @SerializedName("nativeName")
    private String nativeName;

    protected LanguageId(Parcel in) {
        _id = in.readString();
        name = in.readString();
        nativeName = in.readString();
    }

    public static final Creator<LanguageId> CREATOR = new Creator<LanguageId>() {
        @Override
        public LanguageId createFromParcel(Parcel in) {
            return new LanguageId(in);
        }

        @Override
        public LanguageId[] newArray(int size) {
            return new LanguageId[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(name);
        parcel.writeString(nativeName);
    }
}
