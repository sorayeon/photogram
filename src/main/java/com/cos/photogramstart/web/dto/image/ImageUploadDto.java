package com.cos.photogramstart.web.dto.image;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadDto {
    private MultipartFile file;
    private String cation;

    public Image toEntity(String postImageUrl, User user) {
        return Image.builder()
                .caption(cation)
                .postImageUrl(postImageUrl)
                .user(user)
                .build();
    }
}
