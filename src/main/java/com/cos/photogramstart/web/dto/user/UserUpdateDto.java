package com.cos.photogramstart.web.dto.user;

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

}
