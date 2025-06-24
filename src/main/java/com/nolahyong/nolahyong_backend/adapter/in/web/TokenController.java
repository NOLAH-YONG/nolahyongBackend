package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.RefreshTokenRequest;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.TokenResponse;
import com.nolahyong.nolahyong_backend.adapter.out.token.RefreshTokenEntity;
import com.nolahyong.nolahyong_backend.application.service.RefreshTokenService;
import com.nolahyong.nolahyong_backend.application.service.TokenService;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletResponse response
    ) {
        // Refresh Token 검증
        if (!tokenService.validateToken(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }

        // 사용자 정보 추출
        UUID userId = tokenService.getUserIdFromToken(request.getRefreshToken());
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        // 저장소에서 Refresh Token 확인
        Optional<RefreshTokenEntity> tokenEntity = refreshTokenService.findByRefreshToken(
                request.getRefreshToken()
        );
        if (tokenEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token not found");
        }

        // 새로운 Access Token 발급
        User user = userOpt.get();
        String newAccessToken = tokenService.generateAccessToken(user);

        // 토큰 로테이션: 필요시 새 Refresh Token 발급
        String newRefreshToken = null;
        if (shouldRotateRefreshToken(tokenEntity.get())) {
            newRefreshToken = tokenService.generateRefreshToken(user);
            refreshTokenService.saveRefreshToken(
                    user.getUserId(),
                    newRefreshToken,
                    tokenEntity.get().getDeviceFingerprint()
            );
        }

        // 응답 설정
        setSecureCookies(response, newAccessToken, newRefreshToken);
        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }

    private boolean shouldRotateRefreshToken(RefreshTokenEntity tokenEntity) {
        Instant now = Instant.now();
        Instant threeDaysLater = now.plus(3, ChronoUnit.DAYS);

        // LocalDateTime -> Instant 변환
        Instant expiresAtInstant = tokenEntity.getExpiresAt()
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return expiresAtInstant.isBefore(threeDaysLater);
    }

    private void setSecureCookies(HttpServletResponse response,
                                  String accessToken, String refreshToken) {
        if (accessToken != null) {
            Cookie cookie = new Cookie("access_token", accessToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) tokenService.getAccessTokenValidity());
            response.addCookie(cookie);
        }

        if (refreshToken != null) {
            Cookie cookie = new Cookie("refresh_token", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/api/auth/refresh");
            cookie.setMaxAge((int) tokenService.getRefreshTokenValidity());
            response.addCookie(cookie);
        }
    }
}