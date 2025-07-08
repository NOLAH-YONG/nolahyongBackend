package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new MyPageResponse(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }

    // 앞으로 추가될 메서드
    // public void updateProfile(...);
    // public List<String> getSelectedCategories(UUID userId);
}