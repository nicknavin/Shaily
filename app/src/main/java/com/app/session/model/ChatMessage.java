package com.app.session.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;


public class ChatMessage{

	@SerializedName("reciverId")
	private String reciverId;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("senderId")
	private String senderId;

	@SerializedName("senderName")
	private String senderName;

	@SerializedName("reciverName")
	private String reciverName;

	@SerializedName("__v")
	private int V;

	@SerializedName("isRead")
	private boolean isRead;

	@SerializedName("_id")
	private String id;

	@SerializedName("message")
	private String message;

	@SerializedName("reciverProfileUrl")
	private String reciverProfileUrl;

	@SerializedName("senderProfileUrl")
	private String senderProfileUrl;



	public void setReciverId(String reciverId){
		this.reciverId = reciverId;
	}

	public String getReciverId(){
		return reciverId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSenderId(String senderId){
		this.senderId = senderId;
	}

	public String getSenderId(){
		return senderId;
	}

	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	public String getSenderName(){
		return senderName;
	}

	public void setReciverName(String reciverName){
		this.reciverName = reciverName;
	}

	public String getReciverName(){
		return reciverName;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setIsRead(boolean isRead){
		this.isRead = isRead;
	}

	public boolean isIsRead(){
		return isRead;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

	public String getReciverProfileUrl() {
		return reciverProfileUrl;
	}

	public void setReciverProfileUrl(String reciverProfileUrl) {
		this.reciverProfileUrl = reciverProfileUrl;
	}

	public String getSenderProfileUrl() {
		return senderProfileUrl;
	}

	public void setSenderProfileUrl(String senderProfileUrl) {
		this.senderProfileUrl = senderProfileUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChatMessage)) return false;
		ChatMessage that = (ChatMessage) o;
		return getV() == that.getV() &&
				isRead() == that.isRead() &&
				getReciverId().equals(that.getReciverId()) &&
				getCreatedAt().equals(that.getCreatedAt()) &&
				getSenderId().equals(that.getSenderId()) &&
				getSenderName().equals(that.getSenderName()) &&
				getReciverName().equals(that.getReciverName()) &&
				getId().equals(that.getId()) &&
				getMessage().equals(that.getMessage()) &&
				getReciverProfileUrl().equals(that.getReciverProfileUrl()) &&
				getSenderProfileUrl().equals(that.getSenderProfileUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getReciverId(), getCreatedAt(), getSenderId(), getSenderName(), getReciverName(), getV(), isRead(), getId(), getMessage(), getReciverProfileUrl(), getSenderProfileUrl());
	}
}