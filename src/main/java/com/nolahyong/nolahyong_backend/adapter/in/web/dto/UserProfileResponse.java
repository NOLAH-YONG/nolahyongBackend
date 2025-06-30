package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private UUID userId;
    private String email;
    private String nickname;
    private String provider;
}