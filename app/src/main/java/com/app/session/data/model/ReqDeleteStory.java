package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReqDeleteStory implements Serializable {

	@SerializedName("story_id")
	private String storyId;

	@SerializedName("story_provider")
	private String story_provider;


	public void setStoryId(String storyId){
		this.storyId = storyId;
	}

	public String getStoryId(){
		return storyId;
	}

	public String getStory_provider() {
		return story_provider;
	}

	public void setStory_provider(String story_provider) {
		this.story_provider = story_provider;
	}

}