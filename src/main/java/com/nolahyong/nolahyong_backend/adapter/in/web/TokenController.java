package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.*;
import com.nolahyong.nolahyong_backend.application.service.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {
    private final AuthUseCase authUseCase;

    @PostMapping("/oauth/login")
    public ResponseEntity<TokenResponse> socialLogin(@RequestBody SnsLoginRequest request) {
        TokenResponse response = authUseCase.socialLogin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody RefreshTokenRequest request) {
        TokenResponse response = authUseCase.refreshTokens(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        authUseCase.logout(request);
        return ResponseEntity.ok().build();
    }
}