package com.nolahyong.nolahyong_backend.application.port.in;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.CategoryResponse;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MyPageUseCase {
    MyPageResponse getMyPage(UUID userId);

    void updateProfile(UUID userId, String nickname, MultipartFile profileImage, boolean removeImage);

    List<CategoryResponse> getSelectedCategories(UUID userId);

    void updateSelectedCategories(UUID userId, List<String> categoryCodes);
}

