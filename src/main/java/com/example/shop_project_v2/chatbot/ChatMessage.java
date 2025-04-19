package com.example.shop_project_v2.chatbot;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.shop_project_v2.chatbot.ChatMessage;

import lombok.Data;

@Document(collection = "chat_messages")
@Data
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String sender;  
    private String content;
    private Instant timestamp;
}
