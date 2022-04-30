package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{id}")
    public String profile(@PathVariable int id, Model model) {
        User userEntity = userService.회원프로필(id);

        model.addAttribute("user", userEntity);

        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 1. 추천
        log.info("세션정보 : {}", principalDetails.getUser());

        // 2. X
//        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("직접찾은 세션정보 : {}", principal.getUser());

//        model.addAttribute("principal", principalDetails.getUser());

        return "user/update";
    }
}
