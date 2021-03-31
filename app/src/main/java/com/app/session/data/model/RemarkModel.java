package com.app.session.data.model;

/**
 * Created by ITEGRITYPRO on 19-04-2018.
 */

public class RemarkModel
{
    private String consultant_cd;

    private String client_cd;

    private String the_date;

    private String remark_text;

    private String consultant_name;

    private String remarks_id;

    private String client_name;

    private String remark_type;

    private String download_status;
    private boolean isSelected;


    public String getDownload_status() {
        return download_status;
    }

    public void setDownload_status(String download_status) {
        this.download_status = download_status;
    }

    public String getConsultant_cd() {
        return consultant_cd;
    }

    public void setConsultant_cd(String consultant_cd) {
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

    public String getRemark_text() {
        return remark_text;
    }

    public void setRemark_text(String remark_text) {
        this.remark_text = remark_text;
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

    public String getRemarks_id() {
        return remarks_id;
    }

    public void setRemarks_id(String remarks_id) {
        this.remarks_id = remarks_id;
    }

    public String getRemark_type() {
        return remark_type;
    }

    public void setRemark_type(String remark_type) {
        this.remark_type = remark_type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
