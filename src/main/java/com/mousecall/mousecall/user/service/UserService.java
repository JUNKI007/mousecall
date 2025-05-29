package com.mousecall.mousecall.user.service;


import com.mousecall.mousecall.user.domain.User;
import com.mousecall.mousecall.user.dto.UserJoinRequest;
import com.mousecall.mousecall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(UserJoinRequest userJoinRequest){
        if (userRepository.existsByUsername(userJoinRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        User user = User.builder()
                .username(userJoinRequest.getUsername())
                .password(userJoinRequest.getPassword())
                .role("USER")
                .build();

        userRepository.save(user);
    }


}
