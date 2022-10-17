package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.storage.AmazonS3ResourceStorage;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AmazonS3ResourceStorage amazonS3ResourceStorage;

    @Transactional(readOnly = true)
    public UserProfileDto 회원프로필(int pageUserId, int principalId) {
        UserProfileDto dto = new UserProfileDto();

        // SELECT * FROM image WHERE userId = :userId
        User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId); // true 는 페이지 주인, false 는 주인이 아님
        dto.setImageCount(userEntity.getImages().size());

        int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);

        return dto;
    }

    @Transactional
    public User 회원수정(int id, UserUpdateDto dto) {
        // 1. 영속화
        // Optional 1. 무조건 찾았다. 걱정마 get(), 2. 못찾았어 익셉션 발동시킬께 orElseThrow()
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new CustomValidationApiException("찾을 수 없는 id 입니다."));

        String rawPassword = dto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        dto.setPassword(encPassword);

        userEntity.updateMyAccount(dto);
        // 2. 영속화된 오브젝트를 수정 - 더티체킹

        return userEntity; // 더티체킹이 일어나서 업데이트가 완료됨
    }

    @Transactional
    public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
        String imageFileName = amazonS3ResourceStorage.store(profileImageFile);

        log.info("이미지 파일이름 : {}", imageFileName);

        User userEntity = userRepository.findById(principalId).orElseThrow(() -> {
            throw new CustomApiException("유저를 찾을 수 없습니다.");
        });

        userEntity.setProfileImageUrl(imageFileName);
        // 더티체킹으로 업데이트 됨
        return userEntity;
    }
}
