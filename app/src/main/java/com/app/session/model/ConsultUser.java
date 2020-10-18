package com.app.session.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ConsultUser {

	@SerializedName("user_name")
	public String userName;

	@SerializedName("imageUrl")
	public String imageUrl;


	@SerializedName("login_user_id")
	public String loginUserId;

	@SerializedName("_id")
	public String id;

	@SerializedName("country_id")
	public CountryId countryId;


	@SerializedName("briefCV")
	public List<BriefCVUser> briefCV;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CountryId getCountryId() {
		return countryId;
	}

	public void setCountryId(CountryId countryId) {
		this.countryId = countryId;
	}

	public List<BriefCVUser> getBriefCV() {
		return briefCV;
	}

	public void setBriefCV(List<BriefCVUser> briefCV) {
		this.briefCV = briefCV;
	}
}