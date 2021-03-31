package com.app.session.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CurrencyBody implements Parcelable {

    @SerializedName("_id")
    private String _id;
    @SerializedName("currency_cd")
    private String currency_cd;
    @SerializedName("currency_name")
    private String currency_name;

    protected CurrencyBody(Parcel in) {
        _id = in.readString();
        currency_cd = in.readString();
        currency_name = in.readString();
    }

    public static final Creator<CurrencyBody> CREATOR = new Creator<CurrencyBody>() {
        @Override
        public CurrencyBody createFromParcel(Parcel in) {
            return new CurrencyBody(in);
        }

        @Override
        public CurrencyBody[] newArray(int size) {
            return new CurrencyBody[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCurrency_cd() {
        return currency_cd;
    }

    public void setCurrency_cd(String currency_cd) {
        this.currency_cd = currency_cd;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(currency_cd);
        parcel.writeString(currency_name);
    }
}
