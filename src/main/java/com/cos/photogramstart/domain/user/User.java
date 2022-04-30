package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.image.Image;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(DB)할 수 있는 API 를 제공)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // 디비에 테이블을 생성
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다
    private Integer id;

    @Column(length = 20, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    private String website; // 웹 사이트
    private String bio; // 자기 소개
    @Column(nullable = false)
    private String email;
    private String phone;
    private String gender;

    private String profileImageUrl; // 사진
    private String role; // 권한

    // 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지마
    // User 를 Select 할 때 해당 User id로 등록된 image 들을 다 가져와
    // Lazy = User 를 Select 할 때 해당 User id로 등록된 image 들을 가져오지마 - 대신 getImages() 함수의 image 들이 호출될 때 가져와!!
    // Eager = User 를 Select 할 때 해당 User id로 등록된 image 들을 전부 Join 해서 가져와!!
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Image> images;

    private LocalDateTime createDate;

    @PrePersist // 디비에 INSERT 되기 직전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
