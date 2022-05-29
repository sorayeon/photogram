package com.cos.photogramstart.config.oauth;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2DetailsService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 서비스 탐");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2 {}", oAuth2User.getAttributes());

        Map<String, Object> userInfo = oAuth2User.getAttributes();
        String username = "fb_" + (String) userInfo.get("id");

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) { // 페이스북 최초 로그인
            String password =  new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            User user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .name(name)
                    .build();

            userEntity = userRepository.save(user);

        }

        return new PrincipalDetails(userEntity, userInfo);
    }
}
