package com.spig.spig.domain.user.service;

import com.spig.spig.domain.user.dto.JoinRequestDto;
import com.spig.spig.domain.user.dto.LoginRequestDto;
import com.spig.spig.domain.user.dto.LoginResponseDto;

public interface UserService {

    public void join(JoinRequestDto dto);

    public LoginResponseDto login(LoginRequestDto dto);
}
