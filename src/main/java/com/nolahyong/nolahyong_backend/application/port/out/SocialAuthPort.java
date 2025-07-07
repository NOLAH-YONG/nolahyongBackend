package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.domain.model.User;

public interface SocialAuthPort {
    User authenticate(String provider, String accessToken);
}
