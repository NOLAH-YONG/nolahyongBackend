package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.common.util.EncryptionUtil;
import com.nolahyong.nolahyong_backend.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TokenService {

    private final SecretKey secretKey;
    @Getter
    private final long accessTokenValidity;
    @Getter
    private final long refreshTokenValidity;

    @Value("${refresh.token.encryption-key}")
    private String refreshTokenEncryptionKey;

    public TokenService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity
    ) {
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenValidity);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenValidity);
    }

    // refreshToken 암호화
    public String encryptRefreshToken(String refreshToken) {
        try {
            return EncryptionUtil.encrypt(refreshTokenEncryptionKey, refreshToken);
        } catch (Exception e) {
            log.error("Refresh Token 암호화 실패", e);
            throw new RuntimeException("Refresh Token 암호화 실패", e);
        }
    }

    // refreshToken을 복호화
    public String decryptRefreshToken(String encryptedRefreshToken) {
        try {
            return EncryptionUtil.decrypt(refreshTokenEncryptionKey, encryptedRefreshToken);
        } catch (Exception e) {
            log.error("Refresh Token 복호화 실패", e);
            throw new RuntimeException("Refresh Token 복호화 실패", e);
        }
    }

    private String buildToken(User user, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity * 1000);

        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("email", user.getEmail())
                .claim("nickname", user.getNickname())
                .claim("provider", user.getProvider().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }
}