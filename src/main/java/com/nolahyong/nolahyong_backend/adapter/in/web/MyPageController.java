package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import com.nolahyong.nolahyong_backend.application.port.in.MyPageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageUseCase myPageUseCase;

    @GetMapping("/profile")
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal(expression = "id") UUID userId) {
        return ResponseEntity.ok(myPageUseCase.getMyPage(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "removeImage", defaultValue = "false") boolean removeImage
    ) {
        myPageUseCase.updateProfile(userId, nickname, profileImage, removeImage);
        return ResponseEntity.ok().build();
    }
}
