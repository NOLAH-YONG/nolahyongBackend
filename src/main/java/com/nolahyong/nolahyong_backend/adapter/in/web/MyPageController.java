package com.nolahyong.nolahyong_backend.adapter.in.web;

import com.nolahyong.nolahyong_backend.adapter.in.web.dto.MyPageResponse;
import com.nolahyong.nolahyong_backend.adapter.in.web.dto.UpdateProfileRequest;
import com.nolahyong.nolahyong_backend.application.service.MyPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService; // ★ 핵심: 내부의 MyPageUseCase 포트에 의존


    @GetMapping
    public ResponseEntity<MyPageResponse> getMyPage(@AuthenticationPrincipal(expression = "id") UUID userId) {
        return ResponseEntity.ok(myPageService.getMyPage(userId));
    }


}