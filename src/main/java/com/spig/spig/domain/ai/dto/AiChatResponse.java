package com.spig.spig.domain.ai.dto;

import lombok.Getter;

@Getter
public class AiChatResponse {
    String message;

    public AiChatResponse(String message) {
        this.message = message;
    }
}
