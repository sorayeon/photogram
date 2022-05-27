package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.common.BaseEntity;
import com.cos.photogramstart.domain.common.BaseTimeEntity;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(DB)할 수 있는 API 를 제공)
@Entity // 디비에 테이블을 생성
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "username"})
@ToString(exclude = "images")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;
    @Column(name = "password", length = 64, nullable = false)
    private String password;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "website", length = 150)
    private String website; // 웹 사이트
    @Column(name = "bio", length = 1500)
    private String bio; // 자기 소개
    @Column(name = "email", length = 80, nullable = false)
    private String email;
    @Column(name = "phone", length = 15)
    private String phone;
    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "profileImageUrl", length = 150)
    private String profileImageUrl; // 사진
    private String role; // 권한

    // 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지마
    // User 를 Select 할 때 해당 User id로 등록된 image 들을 다 가져와
    // Lazy = User 를 Select 할 때 해당 User id로 등록된 image 들을 가져오지마 - 대신 getImages() 함수의 image 들이 호출될 때 가져와!!
    // Eager = User 를 Select 할 때 해당 User id로 등록된 image 들을 전부 Join 해서 가져와!!
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Image> images;

    @Builder
    public User(String username, String password, String name, String email, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = "ROLE_USER";
    }

//    @PrePersist // 디비에 INSERT 되기 직전에 실행
//    public void createDate() {
//        this.createDate = LocalDateTime.now();
//    }

    public void updateMyAccount(UserUpdateDto dto) {
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.website = dto.getWebsite();
        this.bio = dto.getBio();
        this.phone = dto.getPhone();
        this.gender = dto.getGender();
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
