package com.nolahyong.nolahyong_backend.adapter.out.persistence;

import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserToken;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private UUID tokenId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "refresh_token_encrypted", nullable = false, columnDefinition = "TEXT")
    private String refreshTokenEncrypted;

    @Column(name = "device_fingerprint")
    private String deviceFingerprint;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // 도메인 모델 변환
    public UserToken toDomain() {
        return new UserToken(userId, refreshTokenEncrypted, deviceFingerprint, expiresAt);
    }

    public static RefreshTokenEntity fromDomain(UserToken token) {
        return RefreshTokenEntity.builder()
                .userId(token.getUserId())
                .refreshTokenEncrypted(token.getRefreshTokenEncrypted())
                .deviceFingerprint(token.getDeviceFingerprint())
                .expiresAt(token.getExpiresAt())
                .createdAt(OffsetDateTime.now())
                .build();
    }
}