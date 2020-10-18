package com.app.session.model;

/**
 * Created by ITEGRITYPRO on 20-04-2018.
 */

public class ToDoModel {
    private int consultant_cd;

    private String client_cd;

    private String the_date;

    private String consultant_name;

    private String client_name;

    private String to_do_text;

    private String alert_time;

    private String to_do_id;

    public String getTo_do_id() {
        return to_do_id;
    }

    public void setTo_do_id(String to_do_id) {
        this.to_do_id = to_do_id;
    }

    public int getConsultant_cd() {
        return consultant_cd;
    }

    public void setConsultant_cd(int consultant_cd) {
        this.consultant_cd = consultant_cd;
    }

    public String getClient_cd() {
        return client_cd;
    }

    public void setClient_cd(String client_cd) {
        this.client_cd = client_cd;
    }

    public String getThe_date() {
        return the_date;
    }

    public void setThe_date(String the_date) {
        this.the_date = the_date;
    }

    public String getConsultant_name() {
        return consultant_name;
    }

    public void setConsultant_name(String consultant_name) {
        this.consultant_name = consultant_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getTo_do_text() {
        return to_do_text;
    }

    public void setTo_do_text(String to_do_text) {
        this.to_do_text = to_do_text;
    }

    public String getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(String alert_time) {
        this.alert_time = alert_time;
    }
}
