package com.app.session.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class UserStory implements Parcelable{

	@SerializedName("__v")
	public String V;

	@SerializedName("_id")
	public String id;

	@SerializedName("story_provider")
	public String story_provider;

	@SerializedName("subscription_id")
	public SubscriptionId subscriptionId;




	@SerializedName("createdAt")
	public String createdAt;



	@SerializedName("story_type")
	public String storyType;



	@SerializedName("story_text")
	public String storyText;


	@SerializedName("story_title")
	public String storyTitle;



	@SerializedName("story_url")
	public String storyUrl;

	@SerializedName("thumbnail_url")
	private String thumbnail_url;

	@SerializedName("display_doc_name")
	private String display_doc_name;

    @SerializedName("views")
	public String views;

    @SerializedName("daysAgo")
	public String daysAgo;


	@SerializedName("user_id")
	UserDetails userDetails;

	@SerializedName("StoryViewed")
	public boolean storyViewed;


    @SerializedName("read")
	public StoryRead storyRead;

	@SerializedName("isUserFollwoingPeopleStory")
	public boolean isUserFollwoingPeopleStory;


	public UserStory() {

	}

	protected UserStory(Parcel in) {
		V = in.readString();
		id = in.readString();
		createdAt = in.readString();
		subscriptionId = in.readParcelable(SubscriptionId.class.getClassLoader());
		storyType = in.readString();
		storyText = in.readString();
		storyTitle = in.readString();
		storyUrl = in.readString();
		thumbnail_url = in.readString();
		display_doc_name = in.readString();
		views = in.readString();
		daysAgo = in.readString();
		userDetails = in.readParcelable(UserDetails.class.getClassLoader());
		storyViewed = in.readByte() != 0;
		story_provider = in.readString();
		storyRead = in.readParcelable(StoryRead.class.getClassLoader());
		isUserFollwoingPeopleStory = in.readByte() != 0;
	}

	public static final Creator<UserStory> CREATOR = new Creator<UserStory>() {
		@Override
		public UserStory createFromParcel(Parcel in) {
			return new UserStory(in);
		}

		@Override
		public UserStory[] newArray(int size) {
			return new UserStory[size];
		}
	};

	public String getV() {
		return V;
	}

	public void setV(String v) {
		V = v;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public SubscriptionId getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(SubscriptionId subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getStoryType() {
		return storyType;
	}

	public void setStoryType(String storyType) {
		this.storyType = storyType;
	}

	public String getStoryText() {
		return storyText;
	}

	public void setStoryText(String storyText) {
		this.storyText = storyText;
	}

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}

	public String getStoryUrl() {
		return storyUrl;
	}

	public void setStoryUrl(String storyUrl) {
		this.storyUrl = storyUrl;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	public String getDisplay_doc_name() {
		return display_doc_name;
	}

	public void setDisplay_doc_name(String display_doc_name) {
		this.display_doc_name = display_doc_name;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getDaysAgo() {
		return daysAgo;
	}

	public void setDaysAgo(String daysAgo) {
		this.daysAgo = daysAgo;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public boolean isStoryViewed() {
		return storyViewed;
	}

	public void setStoryViewed(boolean storyViewed) {
		this.storyViewed = storyViewed;
	}

	public String getStory_provider() {
		return story_provider;
	}

	public void setStory_provider(String story_provider) {
		this.story_provider = story_provider;
	}

	public StoryRead getStoryRead() {
		return storyRead;
	}

	public void setStoryRead(StoryRead storyRead) {
		this.storyRead = storyRead;
	}

	public boolean isUserFollwoingPeopleStory() {
		return isUserFollwoingPeopleStory;
	}

	public void setUserFollwoingPeopleStory(boolean userFollwoingPeopleStory) {
		isUserFollwoingPeopleStory = userFollwoingPeopleStory;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(V);
		parcel.writeString(id);
		parcel.writeString(createdAt);
		parcel.writeParcelable(subscriptionId, i);
		parcel.writeString(storyType);
		parcel.writeString(storyText);
		parcel.writeString(storyTitle);
		parcel.writeString(storyUrl);
		parcel.writeString(thumbnail_url);
		parcel.writeString(display_doc_name);
		parcel.writeString(views);
		parcel.writeString(daysAgo);
		parcel.writeParcelable(userDetails, i);
		parcel.writeByte((byte) (storyViewed ? 1 : 0));
		parcel.writeString(story_provider);
		parcel.writeParcelable(storyRead, i);
		parcel.writeByte((byte) (isUserFollwoingPeopleStory ? 1 : 0));
	}
}







