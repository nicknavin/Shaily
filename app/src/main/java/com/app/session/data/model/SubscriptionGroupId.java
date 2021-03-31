
package com.app.session.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SubscriptionGroupId {

    @SerializedName("__v")
    private Long _V;
    @Expose
    private String _id;
    @SerializedName("currency_id")
    private String currencyId;
    @SerializedName("group_description")
    private String groupDescription;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("subscription_price")
    private String subscriptionPrice;

    public Long get_V() {
        return _V;
    }

    public void set_V(Long _V) {
        this._V = _V;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(String subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }
}
