package com.spig.spig.domain.user.dto;

import com.spig.spig.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String tokenType;

    public static LoginResponseDto to(String accessToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
