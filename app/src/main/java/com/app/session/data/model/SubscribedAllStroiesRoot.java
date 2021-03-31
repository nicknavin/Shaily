
package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class SubscribedAllStroiesRoot {

    @SerializedName("body")
    private SubscribedAllStroiesBody mBody;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private int mStatus;

    public SubscribedAllStroiesBody getmBody() {
        return mBody;
    }

    public void setmBody(SubscribedAllStroiesBody mBody) {
        this.mBody = mBody;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }



}
