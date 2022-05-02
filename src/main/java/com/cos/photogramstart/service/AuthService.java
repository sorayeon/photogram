package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service // 1. IOC 2. 트랜잭션 관리
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional // Write(Insert, Update, Delete)
    public User 회원가입(SignupDto signupDto) {
        // 회원가입 진행
        String rawPassword = signupDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        // User <- SignUp
        User user = signupDto.toEntity(encPassword);
        User userEntity = userRepository.save(user);

        return userEntity;
    }
}
