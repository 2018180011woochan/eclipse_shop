package com.example.shop_project_v2.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_project_v2.chatbot.service.ChatMessageService;
import com.example.shop_project_v2.chatbot.service.ChatbotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotAPIController {
	private final ChatbotService chatbotService;
	private final ChatMessageService msgService;

//    @PostMapping
//    public ResponseEntity<Map<String, String>> handleChatbot(@RequestParam("message") String message) {
//        String botResponse = chatbotService.getResponse(message);
//
//        Map<String, String> json = new HashMap<>();
//        json.put("response", botResponse);
//
//        return ResponseEntity.ok(json);
//    }
    
	@PostMapping
	public ResponseEntity<Map<String,String>> handleChatbot(
	    @RequestParam("message") String message,
	    @RequestParam(value="roomId", required=false) String roomId
	) {
	    // roomId 가 넘어왔을 때만 저장
	    if (roomId != null && !roomId.isBlank()) {
	      msgService.save(roomId, "user", message);
	    }

	    String botResponse = chatbotService.getResponse(message);

	    if (roomId != null && !roomId.isBlank()) {
	      msgService.save(roomId, "bot", botResponse);
	    }

	    Map<String,String> json = new HashMap<>();
	    json.put("response", botResponse);
	    return ResponseEntity.ok(json);
	}
}
