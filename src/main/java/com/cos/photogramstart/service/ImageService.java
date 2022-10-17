package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.service.storage.AmazonS3ResourceStorage;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    @Transactional(readOnly = true) // X 영속성 컨텍스트 변경 감지해서, 더티체킹, flush(반영) X
    public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
        Page<Image> images = imageRepository.mStory(principalId, pageable);

        // images 에 좋아요 상태 담기
        images.forEach(image -> {
            image.setLikeCount(image.getLikes().size());
            image.getLikes().forEach(like -> {
                // 해당 이미지에 좋아요 한 사람들을 찾아서 현재 로그인 사람이 좋아요 한것인지 비교
                if (like.getUser().getId() == principalId) {
                    image.setLikeState(true);
                }
            });
        });

        return images;
    }

    @Transactional
    public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {

        String imageFileName = amazonS3ResourceStorage.store(imageUploadDto.getFile());

        log.info("이미지 파일이름 : {}", imageFileName);

        // image 테이블에 저장
        Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());

        imageRepository.save(image);

    }

    @Transactional(readOnly = true)
    public List<Image> 인기사진() {
        return imageRepository.mPopular();
    }
}
