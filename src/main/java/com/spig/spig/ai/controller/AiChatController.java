package com.spig.spig.ai.controller;

import com.spig.spig.ai.dto.AiChatRequest;
import com.spig.spig.ai.dto.AiChatResponse;
import com.spig.spig.ai.service.AiChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stringtemplate.v4.ST;

@RestController
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @GetMapping("/api/ai/chat")
    public AiChatResponse chat(@RequestBody AiChatRequest request) {
        String answer = aiChatService.chat(request);
        return new AiChatResponse(answer);
    }
}