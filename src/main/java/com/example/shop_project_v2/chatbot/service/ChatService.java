package com.example.shop_project_v2.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.ChatRoom;
import com.example.shop_project_v2.chatbot.ChatStatus;

@Service
public class ChatService {
    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();
    
	public ChatRoom createRoom(String userEmail) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setUserEmail(userEmail);
        chatRoom.setStatus(ChatStatus.WAITING.toString());
        rooms.put(roomId, chatRoom);
        return chatRoom;
	}
	
    public void assignAdmin(String roomId, String adminName) {
        ChatRoom room = rooms.get(roomId);
        if (room != null) {
            room.setAdminName(adminName);
            room.setStatus(ChatStatus.ING.toString());
        }
     }
   
    public ChatRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }
    
    public List<ChatRoom> getWaitingAndINGRooms() {
        return rooms.values().stream()
                .filter(r -> "상담 대기중".equals(r.getStatus()) || "상담중".equals(r.getStatus()))
                .collect(Collectors.toList());
    }
    
    public void endChat(String roomId) {
        ChatRoom room = rooms.get(roomId);
        if (room != null) {
            room.setStatus(ChatStatus.CLOSE.toString());
        }
    }
}
