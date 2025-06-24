package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.domain.model.User;

import java.util.UUID;

public interface TokenPort {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    void saveRefreshToken(UUID userId, String refreshToken, String deviceFingerprint);
}
