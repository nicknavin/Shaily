package com.app.session.data.model;


import com.google.gson.annotations.SerializedName;


public class CategoryBody {

	@SerializedName("language_cd")
	public String languageCd;

	@SerializedName("category_name")
	public String categoryName;

	@SerializedName("_id")
	public String id;

	public String getLanguageCd() {
		return languageCd;
	}

	public void setLanguageCd(String languageCd) {
		this.languageCd = languageCd;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}