package com.nolahyong.nolahyong_backend.config;

import com.nolahyong.nolahyong_backend.application.service.RefreshTokenService;
import com.nolahyong.nolahyong_backend.application.service.TokenService;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.security.CustomUserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        // 토큰 생성
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        // 디바이스 지문 생성 (User-Agent + IP 기반 해시)
        String deviceFingerprint = generateDeviceFingerprint(request);

        // 리프레시 토큰 저장 (기존 토큰 삭제 후 새 토큰 저장)
        refreshTokenService.saveRefreshToken(user.getUserId(), refreshToken, deviceFingerprint);

        // 안전한 쿠키 설정
        setSecureCookies(response, accessToken, refreshToken);

        // 모바일 앱을 위한 JSON 응답
        sendMobileResponse(response, accessToken, refreshToken);
    }

    private String generateDeviceFingerprint(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        return DigestUtils.sha256Hex(userAgent + ipAddress);
    }

    private void setSecureCookies(HttpServletResponse response,
                                  String accessToken, String refreshToken) {
        // 액세스 토큰 쿠키 (HTTP Only, Secure)
        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) tokenService.getAccessTokenValidity());
        response.addCookie(accessTokenCookie);

        // 리프레시 토큰 쿠키 (HTTP Only, Secure)
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge((int) tokenService.getRefreshTokenValidity());
        response.addCookie(refreshTokenCookie);
    }

    private void sendMobileResponse(HttpServletResponse response,
                                    String accessToken, String refreshToken) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                String.format("{\"access_token\":\"%s\",\"refresh_token\":\"%s\"}",
                        accessToken, refreshToken)
        );
    }
}
