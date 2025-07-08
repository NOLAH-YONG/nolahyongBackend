package com.nolahyong.nolahyong_backend.adapter.in.web.dto;

/**
 * 마이페이지 조회 응답 DTO
 */
public record MyPageResponse(
        String nickname,
        String profileImageUrl
) {
}