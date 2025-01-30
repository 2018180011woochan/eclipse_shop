package com.example.shop_project_v2.chatbot;

import com.example.shop_project_v2.chatbot.ChatMessage;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String sender;  // user라면 email, admin이라면 "admin"
    private String content;
}
