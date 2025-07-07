package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserToken;

public interface TokenPort {
    UserToken issueToken(User user, String refreshToken, String deviceFingerprint);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
}