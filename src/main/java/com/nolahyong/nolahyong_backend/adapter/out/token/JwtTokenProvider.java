package com.nolahyong.nolahyong_backend.adapter.out.token;

import com.nolahyong.nolahyong_backend.application.port.out.TokenPort;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenPort {
    private final SecretKey secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity
    ) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    @Override
    public UserToken issueToken(User user, String refreshToken, String deviceFingerprint) {
        OffsetDateTime expiresAt = OffsetDateTime.now().plusSeconds(this.refreshTokenValidity);
        return new UserToken(
                user.getUserId(),
                refreshToken,
                deviceFingerprint,
                expiresAt
        );
    }

    @Override
    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenValidity);
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenValidity);
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
}