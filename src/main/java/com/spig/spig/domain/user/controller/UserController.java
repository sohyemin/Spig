package com.spig.spig.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @PostMapping("/signup")
    public void signup(){

    }

    @PostMapping("/login")
    public void login(){

    }

}
