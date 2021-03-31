package com.app.session.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sourabhupadhyay on 28/11/17.
 */

public class Myrepo {
    @SerializedName("Send_messageid")
    @Expose
    private String sendMessageid;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getSendMessageid() {
        return sendMessageid;
    }

    public void setSendMessageid(String sendMessageid) {
        this.sendMessageid = sendMessageid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
