package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}