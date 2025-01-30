package com.example.shop_project_v2.chatbot;

import com.example.shop_project_v2.chatbot.ChatRoom;

import lombok.Data;

@Data
public class ChatRoom {
	private String roomId;        
    private String userId;        
    private String adminId;       
    private String status;  
}
