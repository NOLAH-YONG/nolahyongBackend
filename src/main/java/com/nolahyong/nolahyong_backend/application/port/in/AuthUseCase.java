package com.nolahyong.nolahyong_backend.application.port.in;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.LogoutRequest;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.RefreshTokenRequest;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.SnsLoginRequest;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.TokenResponse;

public interface AuthUseCase {
    TokenResponse socialLogin(SnsLoginRequest request);
    TokenResponse refreshTokens(RefreshTokenRequest request);
    void logout(LogoutRequest request);
}
