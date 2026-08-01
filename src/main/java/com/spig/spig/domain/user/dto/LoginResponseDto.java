package com.spig.spig.domain.user.dto;

import com.spig.spig.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String name;

    public static LoginResponseDto to(User user) {
        return LoginResponseDto.builder()
                .name(user.getName())
                .build();
    }
}
