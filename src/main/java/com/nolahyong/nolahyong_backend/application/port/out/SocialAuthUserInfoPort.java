package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.application.dto.SocialUserInfo;

public interface SocialAuthUserInfoPort {
    SocialUserInfo authenticate(String accessToken);
}
