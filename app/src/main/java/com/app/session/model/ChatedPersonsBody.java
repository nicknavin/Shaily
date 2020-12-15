package com.app.session.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ChatedPersonsBody
{

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
@SerializedName("reciverProfileUrl")
	private String reciverProfileUrl;

@SerializedName("senderProfileUrl")
	private String senderProfileUrl;

	@SerializedName("__v")
	private int V;

	@SerializedName("isRead")
	private boolean isRead;

	@SerializedName("_id")
	private String id;



	@SerializedName("message")
	private String message;

	@SerializedName("messageType")
	private String messageType;

	@SerializedName("displayFileName")
	private String displayFileName;



	public String getReciverProfileUrl() {
		return reciverProfileUrl;
	}

	public void setReciverProfileUrl(String reciverProfileUrl) {
		this.reciverProfileUrl = reciverProfileUrl;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		isRead = read;
	}

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

	public String getSenderProfileUrl() {
		return senderProfileUrl;
	}

	public void setSenderProfileUrl(String senderProfileUrl) {
		this.senderProfileUrl = senderProfileUrl;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public String getDisplayFileName() {
		return displayFileName;
	}

	public void setDisplayFileName(String displayFileName) {
		this.displayFileName = displayFileName;
	}
}