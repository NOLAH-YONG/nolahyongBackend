package com.nolahyong.nolahyong_backend.application.port.in;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MyPageUseCase {
    MyPageResponse getMyPage(UUID userId);
    void updateProfile(UUID userId, String nickname, MultipartFile profileImage, boolean removeImage);
}

