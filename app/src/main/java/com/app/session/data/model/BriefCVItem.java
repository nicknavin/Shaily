package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BriefCVItem implements Serializable {

	@SerializedName("language_cd")
	private String languageCd;

	@SerializedName("brief_cv")
	private String briefCv;

	public String getLanguageCd() {
		return languageCd;
	}

	public void setLanguageCd(String languageCd) {
		this.languageCd = languageCd;
	}

	public String getBriefCv() {
		return briefCv;
	}

	public void setBriefCv(String briefCv) {
		this.briefCv = briefCv;
	}
}