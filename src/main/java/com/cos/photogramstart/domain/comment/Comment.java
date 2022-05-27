package com.cos.photogramstart.domain.comment;

import com.cos.photogramstart.domain.common.BaseTimeEntity;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false)
    private String content;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "imageId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Image image;
}
