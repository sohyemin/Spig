package com.spig.spig.domain.user.dto;

import com.spig.spig.domain.user.entity.User;
import com.spig.spig.domain.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String tokenType;
    private UserRole userRole;

    public static LoginResponseDto to(String accessToken, UserRole role) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userRole(role)
                .build();
    }
}
