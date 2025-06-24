package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.out.token.RefreshTokenEntity;
import com.nolahyong.nolahyong_backend.adapter.out.token.RefreshTokenRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long refreshTokenValidity = 604800L; // 7일

    public void saveRefreshToken(UUID userId, String refreshToken, String deviceFingerprint) {
        refreshTokenRepository.deleteByUserIdAndDeviceFingerprint(userId, deviceFingerprint);
        RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .deviceFingerprint(deviceFingerprint)
                .expiresAt(
                        LocalDateTime.ofInstant(
                                Instant.now().plusSeconds(refreshTokenValidity),
                                ZoneId.systemDefault()
                        )
                )
                .build();
        refreshTokenRepository.save(tokenEntity);
    }

    public Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public void deleteRefreshToken(UUID userId, String deviceFingerprint) {
        refreshTokenRepository.deleteByUserIdAndDeviceFingerprint(userId, deviceFingerprint);
    }
}
