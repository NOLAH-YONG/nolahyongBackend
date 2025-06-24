package com.nolahyong.nolahyong_backend.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SnsLoginRequest {
    private String authorizationCode;
    private String redirectUri;
}