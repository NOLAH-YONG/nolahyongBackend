package com.nolahyong.nolahyong_backend.domain.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserToken {

    private final UUID userId;
    private final String refreshTokenEncrypted;
    private final String deviceFingerprint;
    private final OffsetDateTime expiresAt;

    public boolean isExpired(OffsetDateTime now) {
        return expiresAt.isBefore(now);
    }
}