package com.example.shop_project_v2.chatbot.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    // 메시지 저장 및 처리
	private final ChatService chatService;

    // 유저/관리자가 보낸 메시지를 받아 해당 roomId 구독자들에게 전송
    @MessageMapping("/chat/send") // => /app/chat/send
    @SendTo("/chatroom/messages")
    public ChatMessage sendMessage(ChatMessage message) {

        return message; 
        // 구독한 모든 클라이언트가 이 메시지를 수신(/topic/messages)
    }

    // 상담 종료 
    @MessageMapping("/chat/end")
    @SendTo("/chatroom/chat/end")
    public ChatMessage endChat(ChatMessage message) {
        chatService.endChat(message.getRoomId());
        return message;
    }
}
