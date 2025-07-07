package com.nolahyong.nolahyong_backend.adapter.out.persistence;

import com.nolahyong.nolahyong_backend.adapter.out.token.RefreshTokenRepository;
import com.nolahyong.nolahyong_backend.application.port.out.RefreshTokenPort;
import com.nolahyong.nolahyong_backend.common.util.EncryptionUtil;
import com.nolahyong.nolahyong_backend.domain.model.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenPort {

    @Value("${refresh.token.encryption-key}")
    private String encryptionKey;

    private final RefreshTokenRepository repository;

    @Override
    public void save(UserToken token) {
        repository.save(RefreshTokenEntity.fromDomain(token));
    }

    @Override
    public Optional<UserToken> findByRefreshToken(String refreshToken) {
        return repository.findByRefreshTokenEncrypted(refreshToken)
                .map(RefreshTokenEntity::toDomain);
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        try {
            String encrypted = EncryptionUtil.encrypt(encryptionKey, refreshToken);
            repository.deleteByRefreshTokenEncrypted(encrypted);
        } catch (Exception e) {
            // 예외 로깅 및 적절한 예외 처리
            log.error("RefreshToken 암호화 실패", e);
            throw new RuntimeException("RefreshToken 암호화 중 오류 발생", e);
        }
    }

    @Override
    public void deleteByUserIdAndDeviceFingerprint(UUID userId, String deviceFingerprint) {
        repository.deleteByUserIdAndDeviceFingerprint(userId, deviceFingerprint);
    }
}