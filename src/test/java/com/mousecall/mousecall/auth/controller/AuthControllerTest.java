package com.mousecall.mousecall.auth.controller;

import com.mousecall.mousecall.auth.dto.LoginRequest;
import com.mousecall.mousecall.user.dto.UserJoinRequest;
import com.mousecall.mousecall.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_WithValidRequest_ReturnsCreated() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        // when
        ResponseEntity<String> response = authController.register(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("회원가입 성공", response.getBody());
        verify(userService).register(request);
    }

    @Test
    void registerAdmin_WithValidRequest_ReturnsCreated() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("admin");
        request.setPassword("password");

        // when
        ResponseEntity<String> response = authController.registerAdmin(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("관리자 회원가입 성공", response.getBody());
        verify(userService).registerAdmin(request);
    }

    @Test
    void login_WithValidCredentials_ReturnsToken() {
        // given
        LoginRequest request = new LoginRequest("user", "pass");
        when(userService.login(request)).thenReturn("mocked-token");

        // when
        ResponseEntity<String> response = authController.login(request);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-token", response.getBody());
        verify(userService).login(request);
    }
}
