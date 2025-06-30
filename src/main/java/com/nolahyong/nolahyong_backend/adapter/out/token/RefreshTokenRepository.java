package com.nolahyong.nolahyong_backend.adapter.out.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    void deleteByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.userId = :userId AND r.deviceFingerprint = :deviceFingerprint")
    void deleteByUserIdAndDeviceFingerprint(@Param("userId") UUID userId, @Param("deviceFingerprint") String deviceFingerprint);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    boolean existsByRefreshToken(String refreshToken);
}