package com.app.session.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserBank implements Parcelable {
    private String bank_name;

    private String account_number;

    private String iban_code;

    private String swift_code;

    private String bank_address;

    private String currency_symbol;

   public UserBank()
    {

    }
    protected UserBank(Parcel in) {
        bank_name = in.readString();
        account_number = in.readString();
        iban_code = in.readString();
        swift_code = in.readString();
        bank_address = in.readString();
        currency_symbol = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bank_name);
        dest.writeString(account_number);
        dest.writeString(iban_code);
        dest.writeString(swift_code);
        dest.writeString(bank_address);
        dest.writeString(currency_symbol);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserBank> CREATOR = new Creator<UserBank>() {
        @Override
        public UserBank createFromParcel(Parcel in) {
            return new UserBank(in);
        }

        @Override
        public UserBank[] newArray(int size) {
            return new UserBank[size];
        }
    };

    public void setBank_name(String bank_name){
        this.bank_name = bank_name;
    }
    public String getBank_name(){
        return this.bank_name;
    }
    public void setAccount_number(String account_number){
        this.account_number = account_number;
    }
    public String getAccount_number(){
        return this.account_number;
    }
    public void setIban_code(String iban_code){
        this.iban_code = iban_code;
    }
    public String getIban_code(){
        return this.iban_code;
    }
    public void setSwift_code(String swift_code){
        this.swift_code = swift_code;
    }
    public String getSwift_code(){
        return this.swift_code;
    }
    public void setBank_address(String bank_address){
        this.bank_address = bank_address;
    }
    public String getBank_address(){
        return this.bank_address;
    }
    public void setCurrency_symbol(String currency_symbol){
        this.currency_symbol = currency_symbol;
    }
    public String getCurrency_symbol(){
        return this.currency_symbol;
    }
}
