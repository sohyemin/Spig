package com.spig.spig.domain.user.controller;

import com.spig.spig.domain.user.dto.JoinRequestDto;
import com.spig.spig.domain.user.dto.LoginRequestDto;
import com.spig.spig.domain.user.dto.LoginResponseDto;
import com.spig.spig.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody JoinRequestDto dto) {
        userService.join(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto dto
    ) {
        return userService.login(dto);
    }

}
