package com.example.shop_project_v2.chatbot.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shop_project_v2.chatbot.ChatMessage;
import com.example.shop_project_v2.chatbot.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repo;

    public ChatMessage save(String roomId, String sender, String content) {
        ChatMessage m = new ChatMessage();
        m.setRoomId(roomId);
        m.setSender(sender);
        m.setContent(content);
        m.setTimestamp(Instant.now());
        return repo.save(m);
    }

    public List<ChatMessage> history(String roomId) {
        return repo.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
