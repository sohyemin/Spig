package com.spig.spig.ai.controller;

import com.spig.spig.ai.dto.AiChatRequest;
import com.spig.spig.ai.dto.AiChatResponse;
import com.spig.spig.ai.service.AiChatService;
import com.spig.spig.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;
    private final RagService ragService;

    @GetMapping("/api/ai/chat")
    public AiChatResponse chat(@RequestBody AiChatRequest request) {
        String answer = aiChatService.chat(request);
        return new AiChatResponse(answer);
    }

    @GetMapping("/api/ai/ask")
    public AiChatResponse ask(@RequestParam String question) {
        return new AiChatResponse(ragService.search(question));
    }
}