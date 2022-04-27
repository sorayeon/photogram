package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserUpdateDto {
    @NotBlank
    private String name; // 필수
    @NotBlank
    @Length(min = 4, max = 20)
    private String password; // 필수
    private String website;
    private String bio;
    private String phone;
    private String gender;

    // 조금 위험함. 코드 수정이 필요할 예정
    public User toEntity() {
        return User.builder()
                .name(name) // 패스워드를 기재 안했으면 문제!! Validation 체크
                .password(password) // 패스워드를 기재 안했으면 문제!! Validation 체크
                .website(website)
                .bio(bio)
                .phone(phone)
                .gender(gender)
                .build();
    }
}
