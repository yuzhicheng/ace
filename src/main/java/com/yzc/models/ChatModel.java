package com.yzc.models;

public class ChatModel {

	private String name;

	private String chatContent;

	private String coordinationId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoordinationId() {
		return coordinationId;
	}

	public void setCoordinationId(String coordinationId) {
		this.coordinationId = coordinationId;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

}
