package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

import lombok.*;

@Getter
@Setter
public class SnsLoginRequest {
    private String provider;
    private String accessToken;
    private String deviceFingerprint;
}