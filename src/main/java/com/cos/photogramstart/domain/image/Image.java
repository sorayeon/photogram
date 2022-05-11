package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.common.BaseTimeEntity;
import com.cos.photogramstart.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Getter
@ToString(exclude = "user")
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "caption", length = 150)
    private String caption; // 오늘 나 너무 피곤해
    @Column(name = "postImageUrl", nullable = false, length = 150)
    private String postImageUrl; // 사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - DB 에 그 저장된 경로를 insert

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @Builder
    public Image(String caption, String postImageUrl, User user) {
        this.caption = caption;
        this.postImageUrl = postImageUrl;
        this.user = user;
    }

    // 이미지 좋아요

    // 댓글
//
//    private LocalDateTime createDate;
//
//    @PrePersist
//    public void createDate() {
//        this.createDate = LocalDateTime.now();
//    }
}
