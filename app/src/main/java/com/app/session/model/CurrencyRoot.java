package com.app.session.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrencyRoot
{
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    @SerializedName("body")
    ArrayList<CurrencyBody> currencyBodyArrayList;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CurrencyBody> getCurrencyBodyArrayList() {
        return currencyBodyArrayList;
    }

    public void setCurrencyBodyArrayList(ArrayList<CurrencyBody> currencyBodyArrayList) {
        this.currencyBodyArrayList = currencyBodyArrayList;
    }
}
