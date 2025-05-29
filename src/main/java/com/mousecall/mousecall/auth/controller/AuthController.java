package com.mousecall.mousecall.auth.controller;

import com.mousecall.mousecall.user.dto.UserJoinRequest;
import com.mousecall.mousecall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserJoinRequest request) {
        userService.register(request);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }
}
