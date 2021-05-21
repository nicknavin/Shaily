package com.app.session.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateSubscriptionGroupDetail
{
    @SerializedName("group_name")
    private String group_name;

    @SerializedName("language_id")
    private String language_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("subscription_price")
    private String subscription_price;

    @SerializedName("currency_id")
    private String currency_id;

    @SerializedName("group_description")
    private String group_description;

    @SerializedName("subscription_id")
    private String subscription_id;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubscription_price() {
        return subscription_price;
    }

    public void setSubscription_price(String subscription_price) {
        this.subscription_price = subscription_price;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }
}
