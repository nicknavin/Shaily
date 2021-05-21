package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UnsubscribedBody
{
    @SerializedName("isUnsubscribed")
    private boolean isUnsubscribed;

    @SerializedName("isEligiblefor24")
    private boolean isEligiblefor24;

    @SerializedName("unsubscribeText")
    private String unsubscribeText;


    public boolean isUnsubscribed() {
        return isUnsubscribed;
    }

    public void setUnsubscribed(boolean unsubscribed) {
        isUnsubscribed = unsubscribed;
    }

    public boolean isEligiblefor24() {
        return isEligiblefor24;
    }

    public void setEligiblefor24(boolean eligiblefor24) {
        isEligiblefor24 = eligiblefor24;
    }

    public String getUnsubscribeText() {
        return unsubscribeText;
    }

    public void setUnsubscribeText(String unsubscribeText) {
        this.unsubscribeText = unsubscribeText;
    }
}
