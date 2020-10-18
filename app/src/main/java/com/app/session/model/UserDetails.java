package com.app.session.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;




public class UserDetails implements Parcelable {

	@SerializedName("user_name")
	private String userName;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("login_user_id")
	private String loginUserId;

	@SerializedName("_id")
	private String id;

   public UserDetails ()
	{

	}

	protected UserDetails(Parcel in) {
		userName = in.readString();
		imageUrl = in.readString();
		loginUserId = in.readString();
		id = in.readString();
	}

	public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
		@Override
		public UserDetails createFromParcel(Parcel in) {
			return new UserDetails(in);
		}

		@Override
		public UserDetails[] newArray(int size) {
			return new UserDetails[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(userName);
		parcel.writeString(imageUrl);
		parcel.writeString(loginUserId);
		parcel.writeString(id);
	}


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

	public static Creator<UserDetails> getCREATOR() {
		return CREATOR;
	}
}