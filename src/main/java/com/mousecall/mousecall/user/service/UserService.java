package com.mousecall.mousecall.user.service;


import com.mousecall.mousecall.auth.dto.LoginRequest;
import com.mousecall.mousecall.auth.token.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
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

    public void registerAdmin(UserJoinRequest userJoinRequest){
        if (userRepository.existsByUsername(userJoinRequest.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }

        User user = User.builder()
                .username(userJoinRequest.getUsername())
                .password(userJoinRequest.getPassword())
                .role("ADMIN")
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
    }

}
