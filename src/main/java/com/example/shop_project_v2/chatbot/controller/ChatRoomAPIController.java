package com.example.shop_project_v2.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.chatbot.ChatRoom;
import com.example.shop_project_v2.chatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatRoomAPIController {
	private final ChatService chatService;
	
    @PostMapping("/connection")
    public ResponseEntity<ChatRoom> connectToAdmin(@RequestBody String userEmail) {
        // 새로운 방 생성 -> WAITING 상태
        ChatRoom chatRoom = chatService.createRoom(userEmail);
        return ResponseEntity.ok(chatRoom);
    }
    
    @GetMapping("/room-status")
    public ResponseEntity<ChatRoom> getRoomStatus(@RequestParam String roomId) {
        ChatRoom room = chatService.getRoom(roomId);
        if (room == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(room);
    }
}
