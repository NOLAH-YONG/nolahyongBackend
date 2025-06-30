package com.nolahyong.nolahyong_backend.application.dto;
import com.nolahyong.nolahyong_backend.domain.model.enums.Provider;
import lombok.*;

@Getter
@Builder
public class SocialUserInfo {
    private final String email;
    private final String nickname;
    private final String providerId;
    private final Provider provider;
}