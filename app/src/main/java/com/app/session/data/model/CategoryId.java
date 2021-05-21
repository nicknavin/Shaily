package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CategoryId implements Parcelable
{
    @SerializedName("_id")
    private String _id;
    @SerializedName("language_cd")
    private String language_cd;
    @SerializedName("category_name")
    private String category_name;

    public CategoryId() {

    }

    protected CategoryId(Parcel in) {
        _id = in.readString();
        language_cd = in.readString();
        category_name = in.readString();
    }

    public static final Creator<CategoryId> CREATOR = new Creator<CategoryId>() {
        @Override
        public CategoryId createFromParcel(Parcel in) {
            return new CategoryId(in);
        }

        @Override
        public CategoryId[] newArray(int size) {
            return new CategoryId[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(language_cd);
        parcel.writeString(category_name);
    }

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
