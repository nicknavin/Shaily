package com.app.session.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UpdateBriefReq  {

	@SerializedName("language_id")
	private String language_id;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("title_name")
	private String titleName;

	@SerializedName("brief_cv")
	private String briefCv;

	public String getLanguage_id() {
		return language_id;
	}

	public void setLanguage_id(String language_id) {
		this.language_id = language_id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setTitleName(String titleName){
		this.titleName = titleName;
	}

	public String getTitleName(){
		return titleName;
	}

	public void setBriefCv(String briefCv){
		this.briefCv = briefCv;
	}

	public String getBriefCv(){
		return briefCv;
	}
}