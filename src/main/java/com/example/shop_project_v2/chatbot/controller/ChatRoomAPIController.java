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
	 public ResponseEntity<ChatRoom> connectToAdmin(@RequestBody String userId) {
	     // 1) 방 생성 (또는 이미 존재하는지 확인)
	     ChatRoom chatRoom = chatService.createRoom(userId);
	
	     // 2) 채팅방 정보 반환
	     return ResponseEntity.ok(chatRoom);
	}
	 
	// User가 폴링할 때 사용: 현재 room 상태 조회
    @GetMapping("/room-status")
    public ResponseEntity<ChatRoom> getRoomStatus(@RequestParam("roomId") String roomId) {
        ChatRoom room = chatService.getRoom(roomId);
        if (room == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
    
    @GetMapping("/active-rooms")
    public ResponseEntity<List<ChatRoom>> getActiveRooms() {
        List<ChatRoom> activeRooms = chatService.getWaitingOrInProgressRooms();
        return ResponseEntity.ok(activeRooms);
    }
}
