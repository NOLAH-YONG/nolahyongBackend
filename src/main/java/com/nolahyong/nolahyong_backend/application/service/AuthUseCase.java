package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.*;
import com.nolahyong.nolahyong_backend.application.port.out.SocialAuthPort;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserToken;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import com.nolahyong.nolahyong_backend.application.port.out.TokenPort;
import com.nolahyong.nolahyong_backend.application.port.out.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final SocialAuthPort socialAuthPort;
    private final TokenPort tokenPort;
    private final RefreshTokenPort refreshTokenPort;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse socialLogin(SnsLoginRequest request) {
        User user = socialAuthPort.authenticate(
                request.getProvider(),
                request.getAccessToken()
        );

        String accessToken = tokenPort.generateAccessToken(user);

        String refreshToken = tokenPort.generateRefreshToken(user);
        refreshTokenPort.deleteByUserIdAndDeviceFingerprint(user.getUserId(), request.getDeviceFingerprint());

        UserToken userToken = tokenPort.issueToken(user, refreshToken, request.getDeviceFingerprint());
        refreshTokenPort.save(userToken);

        boolean isOnboarded = user.isOnboarded();

        return new TokenResponse(accessToken, refreshToken, isOnboarded);
    }

    @Transactional
    public TokenResponse refreshTokens(RefreshTokenRequest request) {
        Optional<UserToken> tokenOpt = refreshTokenPort.findByRefreshToken(request.getRefreshToken());
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        UserToken oldToken = tokenOpt.get();
        User user = userRepository.findById(oldToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newAccessToken = tokenPort.generateAccessToken(user);
        String newRefreshToken = tokenPort.generateRefreshToken(user);

        // 기존 토큰 삭제
        refreshTokenPort.deleteByUserIdAndDeviceFingerprint(user.getUserId(), oldToken.getDeviceFingerprint());

        // 새 토큰 저장
        UserToken newToken = tokenPort.issueToken(user, newRefreshToken, oldToken.getDeviceFingerprint());
        refreshTokenPort.save(newToken);

        return new TokenResponse(newAccessToken, newRefreshToken, user.isOnboarded());
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenPort.deleteByRefreshToken(request.getRefreshToken());
    }
}