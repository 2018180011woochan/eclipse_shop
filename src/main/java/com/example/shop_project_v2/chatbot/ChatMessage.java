package com.example.shop_project_v2.chatbot;

import com.example.shop_project_v2.chatbot.ChatMessage;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String sender;  // "user" or "admin"
    private String content; // 보낼 메시지

}
