package com.example.shop_project_v2.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.ChatRoom;
import com.example.shop_project_v2.chatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat/admin")
@RequiredArgsConstructor
public class AdminChatAPIController {
	private final ChatService chatService;
	private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/waiting-rooms")
    public ResponseEntity<List<ChatRoom>> getWaitingAndINGRooms() {
        return ResponseEntity.ok(chatService.getWaitingAndINGRooms());
    }
    
    @GetMapping("/assign")
    public ResponseEntity<String> assignAdminToRoom(@RequestParam("roomId") String roomId,
                                                    @RequestParam("adminName") String adminName) {
        chatService.assignAdmin(roomId, adminName);
        
        // 상담 시작 메시지 전송
        ChatMessage startMessage = new ChatMessage();
        startMessage.setRoomId(roomId);
        startMessage.setSender(adminName); // 관리자 이름 또는 "admin"
        startMessage.setContent("상담이 시작되었습니다.");
        messagingTemplate.convertAndSend("/chatroom/messages", startMessage);
        
        return ResponseEntity.ok("assigned");
    }
    
    @PostMapping("/end")
    public ResponseEntity<String> endChat(@RequestParam("roomId") String roomId) {
        chatService.endChat(roomId);
        return ResponseEntity.ok("ended");
    }
}
