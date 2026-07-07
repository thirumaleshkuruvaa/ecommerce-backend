package com.ecommerce.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.request.ChatRequest;
import com.ecommerce.backend.response.ChatResponse;
import com.ecommerce.backend.service.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request) {

        ChatResponse response = chatService.processMessage(request.getMessege());

        return ResponseEntity.ok(response);
    }

}