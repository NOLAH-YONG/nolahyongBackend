package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.UserProfileResponse;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    /**
     * 내 프로필 조회
     */
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserProfileResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getProvider().name()
        );
    }

    /**
     * 온보딩 완료 처리
     */
    @Transactional
    public void completeOnboarding(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setProfileCompleted(true);
        userRepository.save(user);
    }

}