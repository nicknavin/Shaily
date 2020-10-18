package com.app.session.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReqDeleteStory implements Serializable {

	@SerializedName("story_id")
	private String storyId;

	public void setStoryId(String storyId){
		this.storyId = storyId;
	}

	public String getStoryId(){
		return storyId;
	}
}