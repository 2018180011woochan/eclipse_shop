package com.example.shop_project_v2.chatbot;

import com.example.shop_project_v2.chatbot.ChatMessage;

import lombok.Data;

@Data
public class ChatMessage {
    private String roomId;
    private String sender;  
    private String content;
}
