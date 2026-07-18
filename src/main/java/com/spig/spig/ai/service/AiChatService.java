package com.spig.spig.ai.service;

import com.spig.spig.ai.dto.AiChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    public AiChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

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