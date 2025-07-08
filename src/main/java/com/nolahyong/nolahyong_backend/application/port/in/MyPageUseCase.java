package com.nolahyong.nolahyong_backend.application.port.in;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;

import java.util.UUID;

public interface MyPageUseCase {
    MyPageResponse getMyPage(UUID userId);
}
