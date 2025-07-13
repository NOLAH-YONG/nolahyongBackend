package com.nolahyong.nolahyong_backend.application.service;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.CategoryResponse;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import com.nolahyong.nolahyong_backend.adapter.out.image.StorageService;
import com.nolahyong.nolahyong_backend.application.port.in.MyPageUseCase;
import com.nolahyong.nolahyong_backend.domain.model.Category;
import com.nolahyong.nolahyong_backend.domain.model.User;
import com.nolahyong.nolahyong_backend.domain.model.UserCategory;
import com.nolahyong.nolahyong_backend.domain.repository.CategoryRepository;
import com.nolahyong.nolahyong_backend.domain.repository.UserCategoryRepository;
import com.nolahyong.nolahyong_backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService implements MyPageUseCase {

    private final UserRepository userRepository;
    private final StorageService storageService;
    private final UserCategoryRepository userCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new MyPageResponse(user.getNickname(), user.getProfileImageUrl());
    }

    @Override
    @Transactional
    public void updateProfile(UUID userId, String nickname, MultipartFile profileImage, boolean removeImage) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNickname(nickname);

        boolean hasOldImage = user.getProfileImageUrl() != null && !user.getProfileImageUrl().isBlank();

        if (removeImage || (profileImage != null && !profileImage.isEmpty())) {
            if (hasOldImage) {
                storageService.delete(user.getProfileImageUrl());
            }
        }

        if (removeImage) {
            user.removeProfileImage();
        } else if (profileImage != null && !profileImage.isEmpty()) {
            String newUrl = storageService.store(userId, profileImage);
            user.updateProfileImage(newUrl);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getSelectedCategories(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userCategoryRepository.findByUser(user).stream().map(uc -> new CategoryResponse(uc.getCategory().getCode(), uc.getCategory().getNameKo())).toList();
    }

    @Override
    @Transactional
    public void updateSelectedCategories(UUID userId, List<String> categoryCodes) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        userCategoryRepository.deleteByUser(user);

        List<Category> categories = categoryRepository.findByCodeIn(categoryCodes);
        for (Category category : categories) {
            userCategoryRepository.save(new UserCategory(user, category));
        }
    }
}