package com.example.shop_project_v2.chatbot;

import com.example.shop_project_v2.chatbot.ChatRoom;

import lombok.Data;

@Data
public class ChatRoom {
	private String roomId;
	private String userEmail;	// user 사용자의 email
	private String adminName;	// admin 사용자의 name
	private String status;
}
