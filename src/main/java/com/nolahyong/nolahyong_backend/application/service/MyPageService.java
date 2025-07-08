package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import com.nolahyong.nolahyong_backend.adapter.out.image.StorageService;
import com.nolahyong.nolahyong_backend.application.port.in.MyPageUseCase;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService implements MyPageUseCase {

    private final UserRepository userRepository;
    private final StorageService storageService;

    @Override
    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new MyPageResponse(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }

    @Override
    @Transactional
    public void updateProfile(UUID userId, String nickname, MultipartFile profileImage, boolean removeImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNickname(nickname);

        if (removeImage) {
            String oldUrl = user.getProfileImageUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                storageService.delete(oldUrl);
            }
            user.removeProfileImage();
        } else if (profileImage != null && !profileImage.isEmpty()) {
            String oldUrl = user.getProfileImageUrl();
            if (oldUrl != null && !oldUrl.isBlank()) {
                storageService.delete(oldUrl);
            }
            String imageUrl = storageService.store(userId, profileImage);
            user.updateProfileImage(imageUrl);
        }
    }
}