package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private boolean isOnboarded;
}
