package com.app.session.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReqUpdateBank implements Serializable {

	@SerializedName("userBank")
	private UserBank userBank;

	@SerializedName("user_id")
	private String userId;

	public void setUserBank(UserBank userBank){
		this.userBank = userBank;
	}

	public UserBank getUserBank(){
		return userBank;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}
}