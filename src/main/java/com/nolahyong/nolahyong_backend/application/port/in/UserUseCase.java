package com.nolahyong.nolahyong_backend.application.port.in;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.UserProfileResponse;

import java.util.UUID;

public interface UserUseCase {
    UserProfileResponse getMyProfile(UUID userId);
    void completeOnboarding(UUID userId);
}
