package com.lels.student.chatroom.bean;

public class LastMessage {
	private String ChatSaveMessage;

	public String getChatSaveMessage() {
		return ChatSaveMessage;
	}

	public void setChatSaveMessage(String chatSaveMessage) {
		ChatSaveMessage = chatSaveMessage;
	}

	@Override
	public String toString() {
		return "LastMessage [ChatSaveMessage=" + ChatSaveMessage + "]";
	}

	public LastMessage(String chatSaveMessage) {
		super();
		ChatSaveMessage = chatSaveMessage;
	}

}
