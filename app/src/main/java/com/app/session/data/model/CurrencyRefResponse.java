package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrencyRefResponse
{

    @SerializedName("currency_ref")
    ArrayList<CurrencyRef> currencyRefsList;

    public ArrayList<CurrencyRef> getCurrencyRefsList() {
        return currencyRefsList;
    }

    public void setCurrencyRefsList(ArrayList<CurrencyRef> currencyRefsList) {
        this.currencyRefsList = currencyRefsList;
    }
}
