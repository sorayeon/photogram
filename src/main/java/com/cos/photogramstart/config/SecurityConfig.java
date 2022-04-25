package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화
        http.csrf().disable(); // csrf 해제
        http.authorizeRequests()
            .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**").authenticated() // 해당 URL 권한
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .loginPage("/auth/signin") // GET
            .loginProcessingUrl("/auth/signin") // POST -> 스크링 시큐리티가 로그인 프로세스 진행
            .defaultSuccessUrl("/");
    }
}
