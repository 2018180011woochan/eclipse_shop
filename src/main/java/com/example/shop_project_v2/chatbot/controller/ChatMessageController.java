package com.example.shop_project_v2.chatbot.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.service.ChatMessageService;
import com.example.shop_project_v2.chatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
	private final ChatService chatService;
	private final ChatMessageService msgService;

    // 유저/관리자가 보낸 메시지를 받아 해당 roomId 구독자들에게 전송
    @MessageMapping("/chat/send") 
    @SendTo("/chatroom/messages")
    public ChatMessage sendMessage(ChatMessage message) {
    	msgService.save(message.getRoomId(), message.getSender(), message.getContent());
        return message; 
    }

    // 상담 종료 
    @MessageMapping("/chat/end")
    @SendTo("/chatroom/chat/end")
    public ChatMessage endChat(ChatMessage message) {
        chatService.endChat(message.getRoomId());
        
        // 종료 메시지도 저장?
        msgService.save(message.getRoomId(), message.getSender(), message.getContent());
        return message;
    }
}
