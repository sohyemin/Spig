package com.spig.spig.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    private String login_id;

    @NotBlank(message = "패스워드를 입력해주세요")
    private String password;
}
