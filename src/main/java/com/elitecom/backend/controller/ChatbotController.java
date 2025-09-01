package com.elitecom.backend.controller;

import com.elitecom.backend.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = chatbotService.processMessage(message);
        return ResponseEntity.ok(Map.of("response", response));
    }
    
    @GetMapping("/domains")
    public ResponseEntity<?> getDomains() {
        return ResponseEntity.ok(Map.of("domains", chatbotService.getDomains()));
    }
}
