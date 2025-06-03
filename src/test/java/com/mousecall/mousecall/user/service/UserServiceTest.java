package com.mousecall.mousecall.user.service;

import com.mousecall.mousecall.user.domain.User;
import com.mousecall.mousecall.user.dto.UserJoinRequest;
import com.mousecall.mousecall.user.repository.UserRepository;
import com.mousecall.mousecall.exception.DuplicateUserException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldSaveUser_whenUsernameIsNotDuplicated() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("newuser");
        request.setPassword("password");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);

        // when
        userService.register(request);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenUsernameIsDuplicated() {
        // given
        UserJoinRequest request = new UserJoinRequest();
        request.setUsername("existinguser");
        request.setPassword("password");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // when & then
        assertThrows(DuplicateUserException.class, () -> userService.register(request));
    }
}
