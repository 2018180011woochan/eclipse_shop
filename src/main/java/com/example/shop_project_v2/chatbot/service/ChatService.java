package com.example.shop_project_v2.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.ChatRoom;

@Service
public class ChatService {
	private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    public ChatRoom createRoom(String userId) {
    	String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setUserId(userId);
        chatRoom.setStatus("WAITING");
        // adminId는 아직 없으므로 null
        rooms.put(roomId, chatRoom);
        return chatRoom;
    }
    
    public ChatRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }
    
    // 방 목록에서 대기/진행중인 방만 조회
    public List<ChatRoom> getWaitingOrInProgressRooms() {
        List<ChatRoom> result = new ArrayList<>();
        for (ChatRoom room : rooms.values()) {
            if ("WAITING".equals(room.getStatus()) || "IN_PROGRESS".equals(room.getStatus())) {
                result.add(room);
            }
        }
        System.out.println("🔍 현재 대기/진행중인 방 목록: " + result.size());
        return result;
    }

    public void assignAdmin(String roomId, String adminId) {
        ChatRoom room = rooms.get(roomId);
        if (room != null) {
            room.setAdminId(adminId);
            room.setStatus("IN_PROGRESS");
        }
    }
    
    public void endChat(String roomId) {
        ChatRoom room = rooms.get(roomId);
        if (room != null) {
            room.setStatus("CLOSED");
            System.out.println("Chat room " + roomId + " is closed");
        }
    }
    
    // 메시지 저장 (일단은 미구현)
    public void saveMessage(ChatMessage message) {
        // DB에 저장하거나, 추적이 필요한 경우 rooms에서 꺼내서 처리
        System.out.println("Message saved: " + message.getContent());
    }

    public Map<String, ChatRoom> getAllRooms() {
        return rooms;
    }
   
}
