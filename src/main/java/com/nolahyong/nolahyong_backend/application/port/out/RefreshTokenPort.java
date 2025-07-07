package com.nolahyong.nolahyong_backend.application.port.out;

import com.nolahyong.nolahyong_backend.domain.model.UserToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenPort {
    void save(UserToken token);
    Optional<UserToken> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
    void deleteByUserIdAndDeviceFingerprint(UUID userId, String deviceFingerprint);
}