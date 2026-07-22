package com.spig.spig.domain.ai.service;

import com.spig.spig.domain.ai.dto.AiChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private final ChatClient chatClient;

    String systemPrompt = """
        한 문장으로만 대답해.
        설명은 하지 마.
        """;

    public String chat(AiChatRequest request) {
        String content = chatClient.prompt()
                .system(systemPrompt)
                .user(request.getMessage())
                .call()
                .content();

        System.out.println("content : "+content);
        return content;
    }
}