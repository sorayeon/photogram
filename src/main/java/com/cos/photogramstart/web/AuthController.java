package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor // final 필드를 DI 할때 사용
@Controller // 1. IOC, 2. 파일을 리턴하는 컨트롤러
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }


    // 회원가입버튼 -> /auth/signup -> /auth/signin
    // 회원가입버튼 X (CSRF 토큰)
    @PostMapping("/auth/signup")
    public String signup(SignupDto signupDto) { // key=value (x-www-form-urlencoded)
        // log.info("signup = {}", signupDto);

        // User <- SignUp
        User user = signupDto.toEntity();
        // log.info("user = {}", user);

        User userEntity = authService.회원가입(user);
        log.info("userEntity = {}", userEntity);

        return "auth/signin";
    }
}
