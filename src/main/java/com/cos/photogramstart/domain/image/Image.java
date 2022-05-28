package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.common.BaseTimeEntity;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Image(int id) {
        this.id = id;
    }

    @Builder
    public Image(String caption, String postImageUrl, User user) {
        this.caption = caption;
        this.postImageUrl = postImageUrl;
        this.user = user;
    }

    // 이미지 좋아요
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likes;

    @Transient // DB에 컬럼이 만들어지지 않는다.
    private boolean likeState;

    public void setLikeState(boolean likeState) {
        this.likeState = likeState;
    }
    @Transient
    private int likeCount;

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    // 댓글
//
//    private LocalDateTime createDate;
//
//    @PrePersist
//    public void createDate() {
//        this.createDate = LocalDateTime.now();
//    }
}
