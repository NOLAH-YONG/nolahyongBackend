package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.*;
import com.nolahyong.nolahyong_backend.adapter.out.token.RefreshTokenEntity;
import com.nolahyong.nolahyong_backend.application.service.RefreshTokenService;
import com.nolahyong.nolahyong_backend.application.service.SocialAuthService;
import com.nolahyong.nolahyong_backend.application.service.TokenService;
import com.nolahyong.nolahyong_backend.application.service.UserService;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final SocialAuthService socialAuthService;
    private final UserService userService;

    // 소셜 로그인 엔드포인트 추가
    @PostMapping("/oauth/login")
    public ResponseEntity<TokenResponse> socialLogin(
            @Valid @RequestBody SnsLoginRequest request
    ) {
        // 소셜 인증 서비스를 통해 사용자 정보 획득
        User user = socialAuthService.authenticate(
                request.getProvider(),
                request.getAccessToken()
        );

        // JWT 토큰 생성
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        // 온보딩 여부 확인
        boolean isOnboarded = userService.isOnboarded(user);

        // 리프레시 토큰 저장
        refreshTokenService.saveRefreshToken(
                user.getUserId(),
                refreshToken,
                request.getDeviceFingerprint() // 클라이언트에서 생성한 디바이스 지문
        );

        return ResponseEntity.ok(
                new TokenResponse(accessToken, refreshToken, isOnboarded)
        );
    }

    // 토큰 재발급 엔드포인트 수정 (JSON 응답)
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(
            @Valid @RequestBody RefreshTokenRequest request
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

        // 토큰 로테이션: 새 Refresh Token 발급
        String newRefreshToken = tokenService.generateRefreshToken(user);
        refreshTokenService.saveRefreshToken(
                user.getUserId(),
                newRefreshToken,
                tokenEntity.get().getDeviceFingerprint()
        );

        // JSON 응답으로 반환
        return ResponseEntity.ok(
                new TokenResponse(newAccessToken, newRefreshToken, false)
        );
    }

    // 로그아웃 엔드포인트 추가
    @PostMapping("/logout")
    public ResponseEntity<BasicResponse> logout(
            @RequestHeader("Authorization") String token,
            @RequestBody LogoutRequest request
    ) {
        String refreshToken = request.getRefreshToken();

        // 리프레시 토큰 무효화
        refreshTokenService.deleteByRefreshToken(refreshToken);

        return ResponseEntity.ok(
                new BasicResponse(true, "Logout successful")
        );
    }

    // 프로필 조회 엔드포인트 추가
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                new UserProfileResponse(
                        user.getUserId(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getProvider().name()
                )
        );
    }
}