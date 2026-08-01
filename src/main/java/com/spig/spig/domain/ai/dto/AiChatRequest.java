package com.spig.spig.domain.ai.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AiChatRequest {

    @NotNull
    @Size(max = 5000, message = "메시지는 5000자를 초과할 수 없습니다.")
    String message;
}
