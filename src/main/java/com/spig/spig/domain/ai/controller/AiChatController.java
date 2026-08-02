package com.spig.spig.domain.ai.controller;

import com.spig.spig.domain.ai.dto.AiChatRequest;
import com.spig.spig.domain.ai.dto.AiChatResponse;
import com.spig.spig.domain.ai.service.AiChatService;
import com.spig.spig.domain.ai.service.RagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public AiChatResponse ask(@Valid @RequestBody AiChatRequest request) {
        return new AiChatResponse(ragService.search(request.getMessage()));
    }
}