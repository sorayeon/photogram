package com.cos.photogramstart.config;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화
        http.csrf().disable(); // csrf 해제
        http.authorizeRequests()
            .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated() // 해당 URL 권한
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .loginPage("/auth/signin") // GET
            .loginProcessingUrl("/auth/signin") // POST -> 스크링 시큐리티가 로그인 프로세스 진행
            .defaultSuccessUrl("/")
            .and()
            .oauth2Login()// form 로그인도 하는데, oauth2 로그인도 할꺼야!
            .userInfoEndpoint()//oauth2 로그인을 하면 최종응답을 회원정보를 바로 받을 수 있다.
            .userService(oAuth2DetailsService);
    }
}
