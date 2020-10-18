package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ITEGRITYPRO on 29-03-2018.
 */

public class AddressLocation implements Parcelable {
    private String user_cd;

    private String branch_no;

    private String address;

    private String location_name;

    private String latitude;

    private String longitude;


    protected AddressLocation(Parcel in) {
        user_cd = in.readString();
        branch_no = in.readString();
        address = in.readString();
        location_name = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public AddressLocation() {

    }

    public static final Creator<AddressLocation> CREATOR = new Creator<AddressLocation>() {
        @Override
        public AddressLocation createFromParcel(Parcel in) {
            return new AddressLocation(in);
        }

        @Override
        public AddressLocation[] newArray(int size) {
            return new AddressLocation[size];
        }
    };

    public String getUser_cd() {
        return user_cd;
    }

    public void setUser_cd(String user_cd) {
        this.user_cd = user_cd;
    }

    public String getBranch_no() {
        return branch_no;
    }

    public void setBranch_no(String branch_no) {
        this.branch_no = branch_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_cd);
        dest.writeString(branch_no);
        dest.writeString(address);
        dest.writeString(location_name);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
