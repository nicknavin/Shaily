package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PurchaseSubscribeGroupRoot
{
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private ArrayList <PurchaseSubscribeGroup> purchaseSubscribeGroupBody;


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

    public ArrayList<PurchaseSubscribeGroup> getPurchaseSubscribeGroupBody() {
        return purchaseSubscribeGroupBody;
    }

    public void setPurchaseSubscribeGroupBody(ArrayList<PurchaseSubscribeGroup> purchaseSubscribeGroupBody) {
        this.purchaseSubscribeGroupBody = purchaseSubscribeGroupBody;
    }
}
