package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LanguageCd implements Serializable {

	@SerializedName("language_cd")
	public List<String> languageCd;

	public List<String> getLanguageCd() {
		return languageCd;
	}

	public void setLanguageCd(List<String> languageCd) {
		this.languageCd = languageCd;
	}
}