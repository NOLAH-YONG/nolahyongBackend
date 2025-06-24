package com.nolahyong.nolahyong_backend.application.dto;

import com.nolahyong.nolahyong_backend.domain.model.User;
import lombok.Builder;

@Builder
public record UserInfo(
        String userId,
        String email,
        String nickname,
        String profileImageUrl
) {
    public static UserInfo from(User user) {
        return UserInfo.builder()
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
