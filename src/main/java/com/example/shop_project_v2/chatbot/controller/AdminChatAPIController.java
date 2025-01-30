package com.example.shop_project_v2.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.chatbot.ChatRoom;
import com.example.shop_project_v2.chatbot.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat/admin")
@RequiredArgsConstructor
public class AdminChatAPIController {
	private final ChatService chatService;

    @GetMapping("/waiting-rooms")
    public ResponseEntity<List<ChatRoom>> getWaitingAndINGRooms() {
        return ResponseEntity.ok(chatService.getWaitingAndINGRooms());
    }
    
    @GetMapping("/assign")
    public ResponseEntity<String> assignAdminToRoom(@RequestParam("roomId") String roomId,
                                                    @RequestParam("adminName") String adminName) {
        chatService.assignAdmin(roomId, adminName);
        return ResponseEntity.ok("assigned");
    }
    
    @PostMapping("/end")
    public ResponseEntity<String> endChat(@RequestParam("roomId") String roomId) {
        chatService.endChat(roomId);
        return ResponseEntity.ok("ended");
    }
}
