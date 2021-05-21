package com.app.session.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;


public class ReqAllUser implements Serializable {

	@SerializedName("language_id")
	private List<String> languageId;

	public void setLanguageId(List<String> languageId){
		this.languageId = languageId;
	}

	public List<String> getLanguageId(){
		return languageId;
	}


}