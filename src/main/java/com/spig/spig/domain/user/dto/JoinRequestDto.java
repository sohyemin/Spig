package com.spig.spig.domain.user.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class JoinRequestDto {

    private String login_id;
    private String password;
    private String name;

}
