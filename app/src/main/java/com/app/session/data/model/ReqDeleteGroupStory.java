package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReqDeleteGroupStory implements Serializable {

	@SerializedName("story_id")
	private String storyId;

	@SerializedName("story_provider")
	private String story_provider;
	@SerializedName("subscription_id")
	private String subscription_id;

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

	public String getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(String subscription_id) {
		this.subscription_id = subscription_id;
	}

	@Override
	public String toString() {
		return "ReqDeleteStory{" +
				"storyId='" + storyId + '\'' +
				", story_provider='" + story_provider + '\'' +
				", subscription_id='" + subscription_id + '\'' +
				'}';
	}
}