package com.nolahyong.nolahyong_backend.application.service.social;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;

public interface SocialAuthProvider {
    SocialUserInfo authenticate(String accessToken);
}
